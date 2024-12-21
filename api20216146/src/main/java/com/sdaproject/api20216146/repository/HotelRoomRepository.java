package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.HotelRoom;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class HotelRoomRepository {

    private final List<HotelRoom> hotelRooms = new ArrayList<>();
    private long currentId = 1L;

    public HotelRoom save(HotelRoom hotelRoom) {
        if (hotelRoom.getId() == null) {
            hotelRoom.setId(currentId++);
            hotelRooms.add(hotelRoom);
        } else {
            // If the hotel room already has an ID, update the existing record if found
            boolean updated = false;
            for (int i = 0; i < hotelRooms.size(); i++) {
                if (hotelRooms.get(i).getId().equals(hotelRoom.getId())) {
                    hotelRooms.set(i, hotelRoom);
                    updated = true;
                    break;
                }
            }
            // If not found by ID, add as new
            if (!updated) {
                hotelRooms.add(hotelRoom);
            }
        }
        return hotelRoom;
    }

    public Optional<HotelRoom> findById(Long id) {
        return hotelRooms.stream()
                .filter(hotelRoom -> hotelRoom.getId().equals(id))
                .findFirst();
    }

    public List<HotelRoom> findAll() {
        return hotelRooms;
    }

    public void deleteById(Long id) {
        hotelRooms.removeIf(hotelRoom -> hotelRoom.getId().equals(id));
    }
}
