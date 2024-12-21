package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Booking;
import com.sdaproject.api20216146.model.HotelRoom;
import com.sdaproject.api20216146.model.Event;
import com.sdaproject.api20216146.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            booking.setBookingDate(new Date());
            booking.setTotalAmount(calculateHotelBookingAmount(hotelRoom, checkInDate, checkOutDate));

            bookings.add(booking);
            return booking;
        } else {
            throw new RuntimeException("Hotel room is not available for the selected dates.");
        }
    }

    public Booking bookEvent(Long userId, Long eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (isEventAvailable(event)) {
            Booking booking = new Booking();
            booking.setId(bookingIdCounter++);
            booking.setUser(user);
            booking.setEvent(event);
            booking.setBookingDate(new Date());
            booking.setTotalAmount(calculateEventBookingAmount(event));

            bookings.add(booking);
            return booking;
        } else {
            throw new RuntimeException("Event tickets are not available.");
        }
    }

    private boolean isRoomAvailable(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        return bookings.stream()
                .filter(booking -> booking.getHotelRoom() != null && booking.getHotelRoom().equals(hotelRoom))
                .noneMatch(booking -> !checkInDate.after(booking.getBookingDate()) && !checkOutDate.before(booking.getBookingDate()));
    }

    private boolean isEventAvailable(Event event) {
        return event.getAvailableTickets() > 0;
    }

    private double calculateHotelBookingAmount(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        long duration = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24); // days
        return hotelRoom.getPricePerNight() * duration;
    }

    private double calculateEventBookingAmount(Event event) {
        return event.getTicketPrice();
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

    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking booking = getBooking(id);
        booking.setUser(updatedBooking.getUser());
        booking.setHotelRoom(updatedBooking.getHotelRoom());
        booking.setEvent(updatedBooking.getEvent());
        booking.setBookingDate(updatedBooking.getBookingDate());
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
