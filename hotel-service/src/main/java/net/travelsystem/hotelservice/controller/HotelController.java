package net.travelsystem.hotelservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.hotelservice.dto.hotel.HotelRequest;
import net.travelsystem.hotelservice.dto.hotel.HotelResponse;
import net.travelsystem.hotelservice.service.HotelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final HotelService service;

    public HotelController(HotelService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<HotelResponse> findAllHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            Pageable pageable) {
        return service.findAllHotels(name, location, pageable);
    }

    @PostMapping
    ResponseEntity<String> saveNewHotel(@RequestBody @Valid HotelRequest request){
        service.createNewHotel(request);
        return new ResponseEntity<>(String.format("Hotel %s a été créé avec succès",request.name()),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<String> updateExistingHotel(@PathVariable Long id,@RequestBody @Valid HotelRequest request){
        service.updateHotel(id, request);
        return new ResponseEntity<>(String.format("Hotel %s a été modifié avec succès",request.name()),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<HotelResponse> removeHotelById(@PathVariable Long id){
        service.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
