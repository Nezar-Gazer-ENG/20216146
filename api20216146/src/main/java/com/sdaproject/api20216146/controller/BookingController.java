package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.Booking;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private List<Booking> bookings = new ArrayList<>();
    private long currentId = 1;

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        booking.setId(currentId++);
        booking.setBookingDate(new Date()); // Set current date for booking
        bookings.add(booking);
        return booking;
    }

    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookings;
    }

    @PutMapping("/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        for (Booking booking : bookings) {
            if (booking.getId().equals(id)) {
                booking.setUser(updatedBooking.getUser());
                booking.setHotelRoom(updatedBooking.getHotelRoom());
                booking.setEvent(updatedBooking.getEvent());
                booking.setBookingDate(updatedBooking.getBookingDate());
                booking.setTotalAmount(updatedBooking.getTotalAmount());
                return booking;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        boolean removed = bookings.removeIf(booking -> booking.getId().equals(id));
        return removed ? "Booking deleted" : "Booking not found";
    }
}
