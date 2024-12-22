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
    public ResponseEntity<List<Booking>> getAllBookings(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(null);
        }
    
        List<Booking> userBookings = bookingService.getAllBookingsForUser(loggedInUser.getId());
        System.out.println("User ID: " + loggedInUser.getId() + ", Bookings: " + userBookings); // Debugging
        return ResponseEntity.ok(userBookings);
    }
    
    

    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(@PathVariable Long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("You must be logged in to view booking details.");
        }

        Booking booking = bookingService.getBooking(id);
        if (booking == null || !booking.getUser().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(404).body("Booking not found or access denied.");
        }

        return ResponseEntity.ok(booking);
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

        Booking existingBooking = bookingService.getBooking(id);
        if (existingBooking == null || !existingBooking.getUser().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(404).body("Booking not found or access denied.");
        }

        Booking result = bookingService.updateBooking(id, updatedBooking);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("You must be logged in to delete a booking.");
        }

        Booking booking = bookingService.getBooking(id);
        if (booking == null || !booking.getUser().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(404).body("Booking not found or access denied.");
        }

        String message = bookingService.deleteBooking(id);
        return ResponseEntity.ok(message);
    }
}
