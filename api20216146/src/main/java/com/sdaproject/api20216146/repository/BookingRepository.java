package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.Booking;
import com.sdaproject.api20216146.model.HotelRoom;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BookingRepository {

    private List<Booking> bookings = new ArrayList<>();
    private long currentId = 1L;

    public Booking save(Booking booking) {
        if (booking.getId() == null) {
            booking.setId(currentId++);
            bookings.add(booking);
        } else {
            boolean found = false;
            for (int i = 0; i < bookings.size(); i++) {
                if (bookings.get(i).getId().equals(booking.getId())) {
                    bookings.set(i, booking);
                    found = true;
                    break;
                }
            }
            if (!found) {
                bookings.add(booking);
            }
        }
        return booking;
    }

    public Optional<Booking> findById(Long id) {
        return bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst();
    }

    public List<Booking> findAll() {
        return bookings;
    }

    public void deleteById(Long id) {
        bookings.removeIf(booking -> booking.getId().equals(id));
    }

    public List<Booking> findBookingsByHotelRoomAndBookingDateBetween(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        return bookings.stream()
                .filter(booking ->
                    booking.getHotelRoom() != null &&
                    booking.getHotelRoom().getId().equals(hotelRoom.getId()) &&
                    !booking.getBookingDate().before(checkInDate) &&
                    !booking.getBookingDate().after(checkOutDate)
                )
                .collect(Collectors.toList());
    }

    public List<Booking> findByUserId(Long userId) {
        return bookings.stream()
                .filter(booking -> booking.getUser() != null && booking.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Booking> findByHotelRoomAndDateRange(HotelRoom hotelRoom, Date checkInDate, Date checkOutDate) {
        return bookings.stream()
                .filter(b -> b.getHotelRoom() != null && b.getHotelRoom().getId().equals(hotelRoom.getId()))
                .filter(b -> b.getCheckInDate() != null && b.getCheckOutDate() != null)
                .filter(b -> b.getCheckInDate().before(checkOutDate) && b.getCheckOutDate().after(checkInDate))
                .collect(Collectors.toList());
    }
}
