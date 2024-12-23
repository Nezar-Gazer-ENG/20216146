package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.HotelRoom;
import com.sdaproject.api20216146.service.HotelRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotelrooms")
public class HotelRoomController {

    @Autowired
    private HotelRoomService hotelRoomService;

    @PostMapping
    public ResponseEntity<?> createHotelRoom(@RequestBody HotelRoom hotelRoom) {
        if (hotelRoom.getName() == null || hotelRoom.getRoomType() == null || hotelRoom.getPricePerNight() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid hotel room details. Ensure all fields are provided and price per night is greater than 0.");
        }
        try {
            HotelRoom createdHotelRoom = hotelRoomService.createHotelRoom(hotelRoom);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHotelRoom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the hotel room: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHotelRoom(@PathVariable Long id) {
        try {
            HotelRoom hotelRoom = hotelRoomService.getHotelRoom(id);
            return ResponseEntity.ok(hotelRoom);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Hotel room not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the hotel room: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllHotelRooms() {
        try {
            List<HotelRoom> hotelRooms = hotelRoomService.getAllHotelRooms();
            if (hotelRooms.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No hotel rooms found.");
            }
            return ResponseEntity.ok(hotelRooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving hotel rooms: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotelRoom(@PathVariable Long id, @RequestBody HotelRoom updatedHotelRoom) {
        if (updatedHotelRoom.getName() == null || updatedHotelRoom.getRoomType() == null || updatedHotelRoom.getPricePerNight() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid updated hotel room details. Ensure all fields are provided and price per night is greater than 0.");
        }
        try {
            HotelRoom hotelRoom = hotelRoomService.updateHotelRoom(id, updatedHotelRoom);
            return ResponseEntity.ok(hotelRoom);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Hotel room not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the hotel room: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotelRoom(@PathVariable Long id) {
        try {
            String result = hotelRoomService.deleteHotelRoom(id);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Hotel room not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the hotel room: " + e.getMessage());
        }
    }
}
