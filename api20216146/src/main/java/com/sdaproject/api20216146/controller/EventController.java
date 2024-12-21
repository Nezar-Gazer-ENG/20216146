package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.Event;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private List<Event> events = new ArrayList<>();
    private long currentId = 1;

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        event.setId(currentId++);
        if (event.getEventDate() == null) {
            event.setEventDate(LocalDate.now());
        }
        events.add(event);
        return event;
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id) {
        return events.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return events;
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        for (Event event : events) {
            if (event.getId().equals(id)) {
                event.setName(updatedEvent.getName());
                event.setLocation(updatedEvent.getLocation());
                event.setEventDate(updatedEvent.getEventDate());
                event.setDescription(updatedEvent.getDescription());
                event.setTicketPrice(updatedEvent.getTicketPrice());
                return event;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable Long id) {
        boolean removed = events.removeIf(event -> event.getId().equals(id));
        return removed ? "Event deleted" : "Event not found";
    }
}
