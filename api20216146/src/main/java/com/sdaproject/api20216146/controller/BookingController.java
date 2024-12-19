package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.Booking;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private List<Booking> bookings = new ArrayList<>();

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        bookings.add(booking);
        return booking;
    }

    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookings.stream().filter(booking -> booking.getId().equals(id)).findFirst().orElse(null);
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookings;
    }

    @PutMapping("/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
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

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        Booking booking = bookings.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
        if (booking != null) {
            bookings.remove(booking);
            return "Booking deleted";
        }
        return "Booking not found";
    }
}
