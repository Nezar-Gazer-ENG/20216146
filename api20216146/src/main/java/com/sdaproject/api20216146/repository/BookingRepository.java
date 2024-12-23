package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.Booking;
import com.sdaproject.api20216146.model.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByHotelRoom(HotelRoom hotelRoom);
    List<Booking> findByUserIdAndHotelRoomIsNotNull(Long userId); 
    List<Booking> findByUserIdAndEventIsNotNull(Long userId);     
}


