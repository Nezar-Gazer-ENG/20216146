package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.HotelRoom;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelRoomService {

    private List<HotelRoom> hotelRooms = new ArrayList<>();

    public HotelRoom createHotelRoom(HotelRoom hotelRoom) {
        hotelRooms.add(hotelRoom);
        return hotelRoom;
    }

    public HotelRoom getHotelRoom(Long id) {
        return hotelRooms.stream().filter(hotelRoom -> hotelRoom.getId().equals(id)).findFirst().orElse(null);
    }

    public List<HotelRoom> getAllHotelRooms() {
        return hotelRooms;
    }

    public HotelRoom updateHotelRoom(Long id, HotelRoom updatedHotelRoom) {
        HotelRoom hotelRoom = hotelRooms.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
        if (hotelRoom != null) {
            hotelRoom.setRoomType(updatedHotelRoom.getRoomType());
            hotelRoom.setRoomNumber(updatedHotelRoom.getRoomNumber());
            hotelRoom.setPricePerNight(updatedHotelRoom.getPricePerNight());
            hotelRoom.setAvailable(updatedHotelRoom.isAvailable());
        }
        return hotelRoom;
    }

    public String deleteHotelRoom(Long id) {
        HotelRoom hotelRoom = hotelRooms.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
        if (hotelRoom != null) {
            hotelRooms.remove(hotelRoom);
            return "Hotel room deleted";
        }
        return "Hotel room not found";
    }
}
