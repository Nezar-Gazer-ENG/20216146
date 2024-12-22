package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Booking;
import com.sdaproject.api20216146.model.HotelRoom;
import com.sdaproject.api20216146.model.Event;
import com.sdaproject.api20216146.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final List<Booking> bookings = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<HotelRoom> hotelRooms = new ArrayList<>();
    private final List<Event> events = new ArrayList<>();
    private long bookingIdCounter = 1;

    public Booking bookHotelRoom(Long userId, Long roomId, Date checkInDate, Date checkOutDate) {
        User user = findUserById(userId);
        HotelRoom hotelRoom = findHotelRoomById(roomId);

        if (isRoomAvailable(hotelRoom, checkInDate, checkOutDate)) {
            Booking booking = new Booking();
            booking.setId(bookingIdCounter++);
            booking.setUser(user);
            booking.setHotelRoom(hotelRoom);
            booking.setCheckInDate(checkInDate);
            booking.setCheckOutDate(checkOutDate);
            booking.setBookingDate(new Date());
            booking.setTotalAmount(calculateHotelBookingAmount(hotelRoom, checkInDate, checkOutDate));

            bookings.add(booking);
            return booking;
        } else {
            throw new RuntimeException("Hotel room is not available for the selected dates.");
        }
    }

    public Booking bookEvent(Long userId, Long eventId, int quantity) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (isEventAvailable(event, quantity)) {
            Booking booking = new Booking();
            booking.setId(bookingIdCounter++);
            booking.setUser(user);
            booking.setEvent(event);
            booking.setBookingDate(new Date());
            booking.setTotalAmount(calculateEventBookingAmount(event, quantity));

            event.setSeatsAvailable(event.getSeatsAvailable() - quantity);

            bookings.add(booking);
            return booking;
        } else {
            throw new RuntimeException("Event tickets are not available.");
        }
    }

    private boolean isRoomAvailable(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        return bookings.stream()
                .filter(booking -> booking.getHotelRoom() != null && booking.getHotelRoom().equals(hotelRoom))
                .noneMatch(booking -> 
                        !(checkOutDate.before(booking.getCheckInDate()) || checkInDate.after(booking.getCheckOutDate())));
    }

    private boolean isEventAvailable(Event event, int quantity) {
        return event.getSeatsAvailable() >= quantity;
    }

    private double calculateHotelBookingAmount(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        long durationInMillis = checkOutDate.getTime() - checkInDate.getTime();
        long days = TimeUnit.DAYS.convert(durationInMillis, TimeUnit.MILLISECONDS);
        return hotelRoom.getPricePerNight() * (days > 0 ? days : 1);
    }

    private double calculateEventBookingAmount(Event event, int quantity) {
        return event.getTicketPrice() * quantity;
    }

    public Booking createBooking(Booking booking) {
        booking.setId(bookingIdCounter++);
        bookings.add(booking);
        return booking;
    }

    public Booking getBooking(Long id) {
        return bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Booking not found."));
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public List<Booking> getAllBookingsForUser(Long userId) {
        return bookings.stream()
                .filter(booking -> booking.getUser() != null && booking.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookings.stream()
                .filter(booking -> booking.getUser() != null && booking.getUser().getId().equals(userId))
                .toList();
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking booking = getBooking(id);
        booking.setUser(updatedBooking.getUser());
        booking.setHotelRoom(updatedBooking.getHotelRoom());
        booking.setEvent(updatedBooking.getEvent());
        booking.setCheckInDate(updatedBooking.getCheckInDate());
        booking.setCheckOutDate(updatedBooking.getCheckOutDate());
        booking.setTotalAmount(updatedBooking.getTotalAmount());
        return booking;
    }

    public String deleteBooking(Long id) {
        Booking booking = getBooking(id);
        bookings.remove(booking);
        return "Booking deleted";
    }

    private User findUserById(Long userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    private HotelRoom findHotelRoomById(Long roomId) {
        return hotelRooms.stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Hotel room not found."));
    }

    private Event findEventById(Long eventId) {
        return events.stream()
                .filter(event -> event.getId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Event not found."));
    }
}
