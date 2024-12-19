package com.sdaproject.api20216146.model;

import java.time.LocalDate;

public class Event {
    private Long id;
    private String name;
    private String location;
    private LocalDate eventDate;
    private String description;
    private double ticketPrice;

    public Event(Long id, String name, String location, LocalDate eventDate, String description, double ticketPrice) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.eventDate = eventDate;
        this.description = description;
        this.ticketPrice = ticketPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
