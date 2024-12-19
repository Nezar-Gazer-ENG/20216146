package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.HotelRoom;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hotelrooms")
public class HotelRoomController {

    private List<HotelRoom> hotelRooms = new ArrayList<>();

    @PostMapping
    public HotelRoom createHotelRoom(@RequestBody HotelRoom hotelRoom) {
        hotelRooms.add(hotelRoom);
        return hotelRoom;
    }

    @GetMapping("/{id}")
    public HotelRoom getHotelRoom(@PathVariable Long id) {
        return hotelRooms.stream().filter(hotelRoom -> hotelRoom.getId().equals(id)).findFirst().orElse(null);
    }

    @GetMapping
    public List<HotelRoom> getAllHotelRooms() {
        return hotelRooms;
    }

    @PutMapping("/{id}")
    public HotelRoom updateHotelRoom(@PathVariable Long id, @RequestBody HotelRoom updatedHotelRoom) {
        HotelRoom hotelRoom = hotelRooms.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
        if (hotelRoom != null) {
            hotelRoom.setRoomType(updatedHotelRoom.getRoomType());
            hotelRoom.setRoomNumber(updatedHotelRoom.getRoomNumber());
            hotelRoom.setPricePerNight(updatedHotelRoom.getPricePerNight());
            hotelRoom.setAvailable(updatedHotelRoom.isAvailable());
        }
        return hotelRoom;
    }

    @DeleteMapping("/{id}")
    public String deleteHotelRoom(@PathVariable Long id) {
        HotelRoom hotelRoom = hotelRooms.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
        if (hotelRoom != null) {
            hotelRooms.remove(hotelRoom);
            return "Hotel room deleted";
        }
        return "Hotel room not found";
    }
}
