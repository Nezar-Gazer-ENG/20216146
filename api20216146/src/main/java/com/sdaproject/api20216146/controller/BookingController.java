package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.Booking;
import com.sdaproject.api20216146.model.User;
import com.sdaproject.api20216146.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("You must be logged in to book.");
        }

        booking.setUser(loggedInUser);
        Booking createdBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(createdBooking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("You must be logged in to update a booking.");
        }

        Booking result = bookingService.updateBooking(id, updatedBooking);
        if (result == null) {
            return ResponseEntity.status(404).body("Booking not found");
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("You must be logged in to delete a booking.");
        }

        String message = bookingService.deleteBooking(id);
        if ("Booking deleted".equals(message)) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.status(404).body(message);
        }
    }
}
