package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.City;
import com.sdaproject.api20216146.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findBySeatsAvailableGreaterThan(int seats);

    List<Event> findByNameContainingIgnoreCase(String name);

    List<Event> findByEventDateAfter(LocalDate date);

    List<Event> findByEventDate(LocalDate date);

    List<Event> findByLocationAndEventDateBetween(City location, LocalDate startDate, LocalDate endDate);

    List<Event> findByLocation(City location);

    List<Event> findByLocationAndEventDateBetweenAndSeatsAvailableGreaterThan(
            City location,
            LocalDate startDate,
            LocalDate endDate,
            int minSeats
    );
    

}
