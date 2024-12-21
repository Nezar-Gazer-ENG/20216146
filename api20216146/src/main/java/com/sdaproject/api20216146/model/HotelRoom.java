package com.sdaproject.api20216146.model;

public class HotelRoom {

    private Long id;
    private String name;
    private String roomType;
    private double pricePerNight;
    private boolean available;

    public HotelRoom() {
    }

    public HotelRoom(String name, String roomType, double pricePerNight, boolean available) {
        this.name = name;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.available = available;
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

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
