package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Event;
import com.sdaproject.api20216146.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(Event event) {
        validateEvent(event);
        return eventRepository.save(event);
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        validateEvent(updatedEvent);
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    existingEvent.setName(updatedEvent.getName());
                    existingEvent.setLocation(updatedEvent.getLocation());
                    existingEvent.setEventDate(updatedEvent.getEventDate());
                    existingEvent.setDescription(updatedEvent.getDescription());
                    existingEvent.setTicketPrice(updatedEvent.getTicketPrice());
                    existingEvent.setSeatsAvailable(updatedEvent.getSeatsAvailable());
                    return eventRepository.save(existingEvent);
                })
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    public String deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return "Event deleted";
        }
        throw new RuntimeException("Event not found with id: " + id);
    }

    public String bookEvent(Long id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity: must be greater than zero");
        }

        Event event = getEvent(id);
        if (event.getSeatsAvailable() >= quantity) {
            event.setSeatsAvailable(event.getSeatsAvailable() - quantity);
            eventRepository.save(event);
            return "Event booked successfully";
        } else {
            throw new RuntimeException("Not enough seats available. Available: " + event.getSeatsAvailable());
        }
    }

    private void validateEvent(Event event) {
        if (event.getName() == null || event.getName().isEmpty()) {
            throw new RuntimeException("Event name is required");
        }
        if (event.getLocation() == null) {
            throw new RuntimeException("Event location is required");
        }
        if (event.getEventDate() == null) {
            throw new RuntimeException("Event date is required");
        }
        if (event.getTicketPrice() <= 0) {
            throw new RuntimeException("Event ticket price must be greater than zero");
        }
        if (event.getSeatsAvailable() < 0) {
            throw new RuntimeException("Event seats available cannot be negative");
        }
    }
}
