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
        return hotelRoomRepository.save(hotelRoom);
    }

    public HotelRoom getHotelRoom(Long id) {
        return hotelRoomRepository.findById(id).orElse(null);
    }

    public List<HotelRoom> getAllHotelRooms() {
        return hotelRoomRepository.findAll();
    }

    public HotelRoom updateHotelRoom(Long id, HotelRoom updatedHotelRoom) {
        Optional<HotelRoom> hotelRoomOptional = hotelRoomRepository.findById(id);
        if (hotelRoomOptional.isPresent()) {
            HotelRoom hotelRoom = hotelRoomOptional.get();
            hotelRoom.setRoomType(updatedHotelRoom.getRoomType());
            hotelRoom.setRoomNumber(updatedHotelRoom.getRoomNumber());
            hotelRoom.setPricePerNight(updatedHotelRoom.getPricePerNight());
            hotelRoom.setAvailable(updatedHotelRoom.isAvailable());
            return hotelRoomRepository.save(hotelRoom);
        }
        return null;
    }

    public String deleteHotelRoom(Long id) {
        Optional<HotelRoom> hotelRoomOptional = hotelRoomRepository.findById(id);
        if (hotelRoomOptional.isPresent()) {
            hotelRoomRepository.delete(hotelRoomOptional.get());
            return "Hotel room deleted";
        }
        return "Hotel room not found";
    }
}
