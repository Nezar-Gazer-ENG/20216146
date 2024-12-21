package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.HotelRoom;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HotelRoomService {

    private final List<HotelRoom> hotelRooms = new ArrayList<>();
    private long currentId = 1L;

    public HotelRoom createHotelRoom(HotelRoom hotelRoom) {
        hotelRoom.setId(currentId++);
        hotelRooms.add(hotelRoom);
        return hotelRoom;
    }

    public HotelRoom getHotelRoom(Long id) {
        return hotelRooms.stream()
                .filter(hotelRoom -> hotelRoom.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<HotelRoom> getAllHotelRooms() {
        return new ArrayList<>(hotelRooms);
    }

    public HotelRoom updateHotelRoom(Long id, HotelRoom updatedHotelRoom) {
        Optional<HotelRoom> existingHotelRoomOpt = hotelRooms.stream()
                .filter(hotelRoom -> hotelRoom.getId().equals(id))
                .findFirst();

        if (existingHotelRoomOpt.isPresent()) {
            HotelRoom existingHotelRoom = existingHotelRoomOpt.get();
            existingHotelRoom.setName(updatedHotelRoom.getName());
            existingHotelRoom.setRoomType(updatedHotelRoom.getRoomType());
            existingHotelRoom.setPricePerNight(updatedHotelRoom.getPricePerNight());
            existingHotelRoom.setAvailable(updatedHotelRoom.isAvailable());
            return existingHotelRoom;
        }

        return null;
    }

    public String deleteHotelRoom(Long id) {
        boolean removed = hotelRooms.removeIf(hotelRoom -> hotelRoom.getId().equals(id));
        return removed ? "Hotel room deleted" : "Hotel room not found";
    }
}
