package net.travelsystem.hotelservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.hotelservice.dto.hotel.HotelDetailsDTO;
import net.travelsystem.hotelservice.dto.room.RoomRequest;
import net.travelsystem.hotelservice.dto.room.RoomResponse;
import net.travelsystem.hotelservice.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<RoomResponse> findHotelRooms(
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double price,
            Pageable pageable) {
        return service.findAllRooms(roomNumber, type, price, pageable); }

    @GetMapping("/{name}/{location}/details")
    @ResponseStatus(HttpStatus.OK)
    HotelDetailsDTO getHotelDetails(
            @PathVariable String name,
            @PathVariable String location,
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double price,
            Pageable pageable) {
        return service.hotelDetails(roomNumber, type, price, name, location, pageable);
    }
    @GetMapping("/allAvailableRooms")
    @ResponseStatus(HttpStatus.OK)
    Page<RoomResponse> findHotelAvailableRooms(
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double price,
            Pageable pageable) {
        return service.findAllAvailableRooms(roomNumber, type, price, pageable);
    }

    @PostMapping
    ResponseEntity<String> saveNewHotelRoom(@RequestBody @Valid RoomRequest request){
        service.createRoom(request);
        return new ResponseEntity<>(String.format("Chambre numéro %s a été créé avec succès",request.roomNumber()),HttpStatus.CREATED);
    }
}
