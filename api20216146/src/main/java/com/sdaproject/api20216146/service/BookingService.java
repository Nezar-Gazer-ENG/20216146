package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Booking;
import com.sdaproject.api20216146.model.HotelRoom;
import com.sdaproject.api20216146.model.Event;
import com.sdaproject.api20216146.model.User;
import com.sdaproject.api20216146.repository.BookingRepository;
import com.sdaproject.api20216146.repository.HotelRoomRepository;
import com.sdaproject.api20216146.repository.EventRepository;
import com.sdaproject.api20216146.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    @Autowired
    private EventRepository eventRepository;

    public Booking createBooking(Booking booking) {
        validateBooking(booking);

        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));
        booking.setUser(user);
        booking.setBookingDate(new Date());

        if (isHotelBooking(booking)) {
            handleHotelBooking(booking);
        } else if (isEventBooking(booking)) {
            handleEventBooking(booking);
        } else {
            throw new RuntimeException("Booking must be associated with either a hotel room or an event.");
        }

        return bookingRepository.save(booking);
    }

    private void validateBooking(Booking booking) {
        if (booking.getUser() == null || booking.getUser().getId() == null) {
            throw new RuntimeException("Booking must have a valid user.");
        }
        if (isHotelBooking(booking) && isEventBooking(booking)) {
            throw new RuntimeException("A booking cannot be associated with both a hotel room and an event.");
        }
    }

    private boolean isHotelBooking(Booking booking) {
        return booking.getHotelRoom() != null && booking.getHotelRoom().getId() != null;
    }

    private boolean isEventBooking(Booking booking) {
        return booking.getEvent() != null && booking.getEvent().getId() != null;
    }

    private void handleHotelBooking(Booking booking) {
        HotelRoom hotelRoom = hotelRoomRepository.findById(booking.getHotelRoom().getId())
                .orElseThrow(() -> new RuntimeException("Hotel room not found."));

        if (!isRoomAvailable(hotelRoom, booking.getCheckInDate(), booking.getCheckOutDate())) {
            throw new RuntimeException("Hotel room is not available for the selected dates.");
        }

        booking.setHotelRoom(hotelRoom);
        booking.setTotalAmount(calculateHotelBookingAmount(hotelRoom, booking.getCheckInDate(), booking.getCheckOutDate()));
    }

    private void handleEventBooking(Booking booking) {
        Event event = eventRepository.findById(booking.getEvent().getId())
                .orElseThrow(() -> new RuntimeException("Event not found."));

        if (!isEventAvailable(event, 1)) {
            throw new RuntimeException("Event tickets are not available.");
        }

        event.setSeatsAvailable(event.getSeatsAvailable() - 1);
        eventRepository.save(event);
        booking.setEvent(event);
        booking.setTotalAmount(calculateEventBookingAmount(event, 1));
    }

    private boolean isRoomAvailable(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        return bookingRepository.findByHotelRoom(hotelRoom).stream()
                .noneMatch(booking -> !(checkOutDate.before(booking.getCheckInDate()) || checkInDate.after(booking.getCheckOutDate())));
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

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found."));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getHotelBookingsForUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .filter(booking -> booking.getHotelRoom() != null)
                .collect(Collectors.toList());
    }

    public List<Booking> getEventBookingsForUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .filter(booking -> booking.getEvent() != null)
                .collect(Collectors.toList());
    }

    public String deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return "Booking deleted";
        }
        return "Booking not found";
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        return bookingRepository.findById(id)
                .map(existingBooking -> {
                    existingBooking.setUser(updatedBooking.getUser());
                    existingBooking.setHotelRoom(updatedBooking.getHotelRoom());
                    existingBooking.setEvent(updatedBooking.getEvent());
                    existingBooking.setCheckInDate(updatedBooking.getCheckInDate());
                    existingBooking.setCheckOutDate(updatedBooking.getCheckOutDate());
                    existingBooking.setTotalAmount(updatedBooking.getTotalAmount());
                    return bookingRepository.save(existingBooking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found."));
    }
}
