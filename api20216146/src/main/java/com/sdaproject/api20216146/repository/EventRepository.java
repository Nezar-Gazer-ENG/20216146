package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.Event;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EventRepository {

    private List<Event> events = new ArrayList<>();
    private long currentId = 1L;

    public Event save(Event event) {
        if (event.getId() == null) {
            event.setId(currentId++);
            events.add(event);
        } else {
            // Update existing event if it exists
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).getId().equals(event.getId())) {
                    events.set(i, event);
                    return event;
                }
            }
            // If event with this ID isn't found, treat it as new
            events.add(event);
        }
        return event;
    }

    public Optional<Event> findById(Long id) {
        return events.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst();
    }

    public List<Event> findAll() {
        return events;
    }

    public void deleteById(Long id) {
        events.removeIf(event -> event.getId().equals(id));
    }
}
