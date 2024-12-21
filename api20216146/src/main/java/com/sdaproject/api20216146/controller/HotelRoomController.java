package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.HotelRoom;
import com.sdaproject.api20216146.service.HotelRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotelrooms")
public class HotelRoomController {

    @Autowired
    private HotelRoomService hotelRoomService;

    @PostMapping
    public ResponseEntity<HotelRoom> createHotelRoom(@RequestBody HotelRoom hotelRoom) {
        if (hotelRoom.getName() == null || hotelRoom.getRoomType() == null || hotelRoom.getPricePerNight() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            HotelRoom createdHotelRoom = hotelRoomService.createHotelRoom(hotelRoom);
            return ResponseEntity.ok(createdHotelRoom);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelRoom> getHotelRoom(@PathVariable Long id) {
        HotelRoom hotelRoom = hotelRoomService.getHotelRoom(id);
        if (hotelRoom != null) {
            return ResponseEntity.ok(hotelRoom);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<HotelRoom>> getAllHotelRooms() {
        List<HotelRoom> hotelRooms = hotelRoomService.getAllHotelRooms();
        if (hotelRooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(hotelRooms);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelRoom> updateHotelRoom(@PathVariable Long id, @RequestBody HotelRoom updatedHotelRoom) {
        if (updatedHotelRoom.getName() == null || updatedHotelRoom.getRoomType() == null || updatedHotelRoom.getPricePerNight() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        HotelRoom hotelRoom = hotelRoomService.updateHotelRoom(id, updatedHotelRoom);
        if (hotelRoom != null) {
            return ResponseEntity.ok(hotelRoom);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotelRoom(@PathVariable Long id) {
        String result = hotelRoomService.deleteHotelRoom(id);
        if ("Hotel room deleted".equals(result)) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }
}
