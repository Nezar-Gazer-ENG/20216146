package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.Booking;
import com.sdaproject.api20216146.model.User;
import com.sdaproject.api20216146.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private static final Logger logger = Logger.getLogger(BookingController.class.getName());

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public ResponseEntity<?> getAllBookings(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.warning("Unauthorized access attempt to view all bookings.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\":\"You must be logged in to view bookings.\"}");
        }

        List<Booking> userBookings = bookingService.getBookingsByUserId(loggedInUser.getId());
        if (userBookings.isEmpty()) {
            logger.info("No bookings found for user with ID: " + loggedInUser.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\":\"No bookings found.\"}");
        }

        logger.info("Successfully retrieved bookings for user with ID: " + loggedInUser.getId());
        return ResponseEntity.ok(userBookings);
    }

    @GetMapping("/hotels")
    public ResponseEntity<?> getHotelBookings(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.warning("Unauthorized access attempt to view hotel bookings.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\":\"You must be logged in to view hotel bookings.\"}");
        }

        List<Booking> hotelBookings = bookingService.getHotelBookingsForUser(loggedInUser.getId());
        if (hotelBookings.isEmpty()) {
            logger.info("No hotel bookings found for user with ID: " + loggedInUser.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\":\"No hotel bookings found.\"}");
        }

        logger.info("Successfully retrieved hotel bookings for user with ID: " + loggedInUser.getId());
        return ResponseEntity.ok(hotelBookings);
    }

    @GetMapping("/events")
    public ResponseEntity<?> getEventBookings(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.warning("Unauthorized access attempt to view event bookings.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\":\"You must be logged in to view event bookings.\"}");
        }

        List<Booking> eventBookings = bookingService.getEventBookingsForUser(loggedInUser.getId());
        if (eventBookings.isEmpty()) {
            logger.info("No event bookings found for user with ID: " + loggedInUser.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\":\"No event bookings found.\"}");
        }

        logger.info("Successfully retrieved event bookings for user with ID: " + loggedInUser.getId());
        return ResponseEntity.ok(eventBookings);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.warning("Unauthorized access attempt to create a booking.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\":\"You must be logged in to create a booking.\"}");
        }

        try {
            if (booking.getHotelRoom() == null && booking.getEvent() == null) {
                logger.warning("Invalid booking request: Missing hotel room or event.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Booking must be associated with either a hotel room or an event.\"}");
            }

            booking.setUser(loggedInUser);
            Booking createdBooking = bookingService.createBooking(booking);
            logger.info("Booking created successfully for user ID: " + loggedInUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
        } catch (IllegalArgumentException e) {
            logger.severe("Invalid booking request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Invalid booking request: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            logger.severe("Error creating booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"Error creating booking: " + e.getMessage() + "\"}");
        }
    }
}
