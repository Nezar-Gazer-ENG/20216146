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

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private HotelRoomRepository hotelRoomRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    public Booking bookHotelRoom(Long userId, Long roomId, Date checkInDate, Date checkOutDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        HotelRoom hotelRoom = hotelRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Hotel room not found."));

        if (isRoomAvailable(hotelRoom, checkInDate, checkOutDate)) {
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setHotelRoom(hotelRoom);
            booking.setBookingDate(new Date()); 
            booking.setTotalAmount(calculateHotelBookingAmount(hotelRoom, checkInDate, checkOutDate));

            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Hotel room is not available for the selected dates.");
        }
    }

    public Booking bookEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found."));

        if (isEventAvailable(event)) {
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setEvent(event);
            booking.setBookingDate(new Date()); 
            booking.setTotalAmount(calculateEventBookingAmount(event));

            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Event tickets are not available.");
        }
    }

    private boolean isRoomAvailable(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        List<Booking> bookings = bookingRepository.findBookingsByHotelRoomAndBookingDateBetween(hotelRoom, checkInDate, checkOutDate);
        return bookings.isEmpty();
    }

    private boolean isEventAvailable(Event event) {
        return event.getTicketPrice() > 0;
    }

    private double calculateHotelBookingAmount(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        long duration = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24); // days
        return hotelRoom.getPricePerNight() * duration;
    }

    private double calculateEventBookingAmount(Event event) {
        return event.getTicketPrice();
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found."));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        booking.setUser(updatedBooking.getUser());
        booking.setHotelRoom(updatedBooking.getHotelRoom());
        booking.setEvent(updatedBooking.getEvent());
        booking.setBookingDate(updatedBooking.getBookingDate());
        booking.setTotalAmount(updatedBooking.getTotalAmount());

        return bookingRepository.save(booking);
    }

    public String deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        bookingRepository.delete(booking);
        return "Booking deleted";
    }
}
