package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.Event;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EventRepository {

    private List<Event> events = new ArrayList<>();

    public Event save(Event event) {
        events.add(event);
        return event;
    }

    public Event findById(Long id) {
        return events.stream().filter(event -> event.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Event> findAll() {
        return events;
    }

    public Event update(Long id, Event updatedEvent) {
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

    public String delete(Long id) {
        Event event = events.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (event != null) {
            events.remove(event);
            return "Event deleted";
        }
        return "Event not found";
    }
}
