package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.HotelRoom;
import com.sdaproject.api20216146.service.HotelRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotelrooms")
public class HotelRoomController {

    @Autowired
    private HotelRoomService hotelRoomService;

    @PostMapping
    public HotelRoom createHotelRoom(@RequestBody HotelRoom hotelRoom) {
        return hotelRoomService.createHotelRoom(hotelRoom);
    }

    @GetMapping("/{id}")
    public HotelRoom getHotelRoom(@PathVariable Long id) {
        return hotelRoomService.getHotelRoom(id);
    }

    @GetMapping
    public List<HotelRoom> getAllHotelRooms() {
        return hotelRoomService.getAllHotelRooms();
    }

    @PutMapping("/{id}")
    public HotelRoom updateHotelRoom(@PathVariable Long id, @RequestBody HotelRoom updatedHotelRoom) {
        return hotelRoomService.updateHotelRoom(id, updatedHotelRoom);
    }

    @DeleteMapping("/{id}")
    public String deleteHotelRoom(@PathVariable Long id) {
        return hotelRoomService.deleteHotelRoom(id);
    }
}
