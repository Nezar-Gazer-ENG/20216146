package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.Event;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private List<Event> events = new ArrayList<>();

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        events.add(event);
        return event;
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id) {
        return events.stream().filter(event -> event.getId().equals(id)).findFirst().orElse(null);
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return events;
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
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

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable Long id) {
        Event event = events.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (event != null) {
            events.remove(event);
            return "Event deleted";
        }
        return "Event not found";
    }
}
