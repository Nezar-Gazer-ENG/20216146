package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.HotelRoom;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HotelRoomRepository {

    private List<HotelRoom> hotelRooms = new ArrayList<>();

    public HotelRoom save(HotelRoom hotelRoom) {
        hotelRooms.add(hotelRoom);
        return hotelRoom;
    }

    public HotelRoom findById(Long id) {
        return hotelRooms.stream().filter(hotelRoom -> hotelRoom.getId().equals(id)).findFirst().orElse(null);
    }

    public List<HotelRoom> findAll() {
        return hotelRooms;
    }

    public HotelRoom update(Long id, HotelRoom updatedHotelRoom) {
        HotelRoom hotelRoom = hotelRooms.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
        if (hotelRoom != null) {
            hotelRoom.setRoomType(updatedHotelRoom.getRoomType());
            hotelRoom.setRoomNumber(updatedHotelRoom.getRoomNumber());
            hotelRoom.setPricePerNight(updatedHotelRoom.getPricePerNight());
            hotelRoom.setAvailable(updatedHotelRoom.isAvailable());
        }
        return hotelRoom;
    }

    public String delete(Long id) {
        HotelRoom hotelRoom = hotelRooms.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
        if (hotelRoom != null) {
            hotelRooms.remove(hotelRoom);
            return "Hotel room deleted";
        }
        return "Hotel room not found";
    }
}
