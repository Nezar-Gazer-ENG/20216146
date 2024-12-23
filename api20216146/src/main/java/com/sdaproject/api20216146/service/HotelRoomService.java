package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.HotelRoom;
import com.sdaproject.api20216146.repository.HotelRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelRoomService {

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    public HotelRoom createHotelRoom(HotelRoom hotelRoom) {
        if (hotelRoom == null || hotelRoom.getPricePerNight() <= 0) {
            throw new IllegalArgumentException("Invalid hotel room details.");
        }
        return hotelRoomRepository.save(hotelRoom);
    }

    public HotelRoom getHotelRoom(Long id) {
        return hotelRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel room not found."));
    }

    public List<HotelRoom> getAllHotelRooms() {
        return hotelRoomRepository.findAll();
    }

    public HotelRoom updateHotelRoom(Long id, HotelRoom updatedHotelRoom) {
        return hotelRoomRepository.findById(id)
                .map(existingHotelRoom -> {
                    if (updatedHotelRoom.getName() != null) {
                        existingHotelRoom.setName(updatedHotelRoom.getName());
                    }
                    if (updatedHotelRoom.getRoomType() != null) {
                        existingHotelRoom.setRoomType(updatedHotelRoom.getRoomType());
                    }
                    if (updatedHotelRoom.getPricePerNight() > 0) {
                        existingHotelRoom.setPricePerNight(updatedHotelRoom.getPricePerNight());
                    }
                    existingHotelRoom.setAvailable(updatedHotelRoom.isAvailable());
                    return hotelRoomRepository.save(existingHotelRoom);
                })
                .orElseThrow(() -> new RuntimeException("Hotel room not found for update."));
    }

    public String deleteHotelRoom(Long id) {
        if (hotelRoomRepository.existsById(id)) {
            hotelRoomRepository.deleteById(id);
            return "Hotel room deleted";
        }
        throw new RuntimeException("Hotel room not found for deletion.");
    }

    public boolean isRoomAvailable(Long roomId) {
        Optional<HotelRoom> room = hotelRoomRepository.findById(roomId);
        if (room.isEmpty() || !room.get().isAvailable()) {
            throw new RuntimeException("Hotel room is not available.");
        }
        return true;
    }
}
