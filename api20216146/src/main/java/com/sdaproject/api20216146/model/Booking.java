package com.sdaproject.api20216146.model;

import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "bookings")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotel_room_id", nullable = true)
    private HotelRoom hotelRoom;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = true)
    private Event event;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "booking_date", nullable = false)
    private Date bookingDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "check_in_date", nullable = true)
    private Date checkInDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "check_out_date", nullable = true)
    private Date checkOutDate;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    public Booking() {}

    public Booking(User user, HotelRoom hotelRoom, Event event, Date bookingDate, Date checkInDate, Date checkOutDate, int quantity, double totalAmount) {
        this.user = user;
        this.hotelRoom = hotelRoom;
        this.event = event;
        this.bookingDate = bookingDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HotelRoom getHotelRoom() {
        return hotelRoom;
    }

    public void setHotelRoom(HotelRoom hotelRoom) {
        this.hotelRoom = hotelRoom;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
