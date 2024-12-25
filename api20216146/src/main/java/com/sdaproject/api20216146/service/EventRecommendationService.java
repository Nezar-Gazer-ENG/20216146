package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.City;
import com.sdaproject.api20216146.model.Event;
import com.sdaproject.api20216146.model.Booking;
import com.sdaproject.api20216146.repository.BookingRepository;
import com.sdaproject.api20216146.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventRecommendationService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getRecommendedEvents(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        List<Event> recommendedEvents = new ArrayList<>();

        if (bookings == null || bookings.isEmpty()) {
            return recommendedEvents; // Return empty if no bookings found
        }

        for (Booking booking : bookings) {
            if (booking.getHotelRoom() != null && booking.getHotelRoom().getLocation() != null) {
                City location = booking.getHotelRoom().getLocation();

                if (booking.getCheckInDate() != null && booking.getCheckOutDate() != null) {
                    LocalDate checkInDate = booking.getCheckInDate()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    LocalDate checkOutDate = booking.getCheckOutDate()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    List<Event> events = eventRepository.findByLocationAndEventDateBetween(location, checkInDate, checkOutDate);
                    if (events != null && !events.isEmpty()) {
                        recommendedEvents.addAll(events);
                    }
                }
            }
        }

        return recommendedEvents;
    }
}
