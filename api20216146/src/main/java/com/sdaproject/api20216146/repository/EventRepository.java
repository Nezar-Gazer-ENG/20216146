package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findBySeatsAvailableGreaterThan(int seats);

    List<Event> findByNameContainingIgnoreCase(String name);

    List<Event> findByEventDateAfter(java.time.LocalDate date);

    List<Event> findByEventDate(java.time.LocalDate date);

}
