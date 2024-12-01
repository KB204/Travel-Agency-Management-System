package net.travelsystem.hotelservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.hotelservice.dto.hotel.HotelDetailsDTO;
import net.travelsystem.hotelservice.dto.room.RoomRequest;
import net.travelsystem.hotelservice.dto.room.RoomResponse;
import net.travelsystem.hotelservice.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<RoomResponse> findHotelRooms() { return service.findAllRooms(); }

    @GetMapping("/{name}/{location}/details")
    @ResponseStatus(HttpStatus.OK)
    HotelDetailsDTO getHotelDetails(@PathVariable String name, @PathVariable String location) {
        return service.hotelDetails(name, location);
    }

    @PostMapping
    ResponseEntity<String> saveNewHotelRoom(@RequestBody @Valid RoomRequest request){
        service.createRoom(request);
        return new ResponseEntity<>(String.format("Chambre numéro %s a été créé avec succès",request.roomNumber()),HttpStatus.CREATED);
    }
}
