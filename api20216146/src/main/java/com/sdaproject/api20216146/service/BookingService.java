package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Booking;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private List<Booking> bookings = new ArrayList<>();

    public Booking createBooking(Booking booking) {
        bookings.add(booking);
        return booking;
    }

    public Booking getBooking(Long id) {
        return bookings.stream().filter(booking -> booking.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking booking = bookings.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
        if (booking != null) {
            booking.setUser(updatedBooking.getUser());
            booking.setHotelRoom(updatedBooking.getHotelRoom());
            booking.setEvent(updatedBooking.getEvent());
            booking.setBookingDate(updatedBooking.getBookingDate());
            booking.setTotalAmount(updatedBooking.getTotalAmount());
        }
        return booking;
    }

    public String deleteBooking(Long id) {
        Booking booking = bookings.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
        if (booking != null) {
            bookings.remove(booking);
            return "Booking deleted";
        }
        return "Booking not found";
    }
}
