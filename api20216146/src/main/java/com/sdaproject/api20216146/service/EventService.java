package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final List<Event> events = new ArrayList<>();
    private long eventIdCounter = 1;

    public Event createEvent(Event event) {
        event.setId(eventIdCounter++);
        events.add(event);
        return event;
    }

    public Event getEvent(Long id) {
        return events.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event event = getEvent(id);
        if (event != null) {
            event.setName(updatedEvent.getName());
            event.setLocation(updatedEvent.getLocation());
            event.setEventDate(updatedEvent.getEventDate());
            event.setDescription(updatedEvent.getDescription());
            event.setTicketPrice(updatedEvent.getTicketPrice());
            return event;
        }
        return null;
    }

    public String deleteEvent(Long id) {
        Event event = getEvent(id);
        if (event != null) {
            events.remove(event);
            return "Event deleted";
        }
        return "Event not found";
    }

    public String bookEvent(Long id, int quantity) {
        Event event = getEvent(id);
        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (event.getSeatsAvailable() >= quantity) {
            event.setSeatsAvailable(event.getSeatsAvailable() - quantity);
            return "Event booked successfully";
        } else {
            throw new RuntimeException("Not enough seats available");
        }
    }
}
