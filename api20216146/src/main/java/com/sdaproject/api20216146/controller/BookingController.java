package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.Booking;
import com.sdaproject.api20216146.model.Event;
import com.sdaproject.api20216146.model.User;
import com.sdaproject.api20216146.service.BookingService;
import com.sdaproject.api20216146.service.EventRecommendationService;
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

    @Autowired
    private EventRecommendationService recommendationService;

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
            return ResponseEntity.ok(List.of());
        }

        logger.info("Successfully retrieved bookings for user with ID: " + loggedInUser.getId());
        return ResponseEntity.ok(userBookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.warning("Unauthorized access attempt to view booking details.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            logger.warning("Booking with ID " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!booking.getUser().getId().equals(loggedInUser.getId())) {
            logger.warning("User ID " + loggedInUser.getId() + " attempted to access booking ID " + id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        logger.info("Successfully retrieved booking details for booking ID: " + id);
        return ResponseEntity.ok(booking);
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
            return ResponseEntity.ok(List.of());
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
            return ResponseEntity.ok(List.of());
        }

        logger.info("Successfully retrieved event bookings for user with ID: " + loggedInUser.getId());
        return ResponseEntity.ok(eventBookings);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendedEvents(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.warning("Unauthorized access attempt to view recommended events.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\":\"You must be logged in to view recommendations.\"}");
        }

        try {
            List<Event> recommendedEvents = recommendationService.getRecommendedEvents(loggedInUser.getId());
            if (recommendedEvents.isEmpty()) {
                logger.info("No recommended events found for user with ID: " + loggedInUser.getId());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No recommended events found.");
            }
            logger.info("Successfully retrieved recommended events for user with ID: " + loggedInUser.getId());
            return ResponseEntity.ok(recommendedEvents);
        } catch (Exception e) {
            logger.severe("Error retrieving recommended events: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"Error retrieving recommendations: " + e.getMessage() + "\"}");
        }
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

            if (booking.getEvent() != null) {
                booking.setHotelRoom(null);
                logger.info("Creating an event booking for user ID: " + loggedInUser.getId());
            }
            if (booking.getHotelRoom() != null) {
                booking.setEvent(null);
                logger.info("Creating a hotel booking for user ID: " + loggedInUser.getId());
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
