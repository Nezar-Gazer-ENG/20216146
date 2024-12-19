package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private List<Event> events = new ArrayList<>();

    public Event createEvent(Event event) {
        events.add(event);
        return event;
    }

    public Event getEvent(Long id) {
        return events.stream().filter(event -> event.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Event> getAllEvents() {
        return events;
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event event = events.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (event != null) {
            event.setName(updatedEvent.getName());
            event.setLocation(updatedEvent.getLocation());
            event.setEventDate(updatedEvent.getEventDate());
            event.setDescription(updatedEvent.getDescription());
            event.setTicketPrice(updatedEvent.getTicketPrice());
        }
        return event;
    }

    public String deleteEvent(Long id) {
        Event event = events.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (event != null) {
            events.remove(event);
            return "Event deleted";
        }
        return "Event not found";
    }
}
