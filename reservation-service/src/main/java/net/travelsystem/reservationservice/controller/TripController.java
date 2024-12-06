package net.travelsystem.reservationservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.reservationservice.dto.trip.TripRequest;
import net.travelsystem.reservationservice.dto.trip.TripResponse;
import net.travelsystem.reservationservice.service.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final TripService service;

    public TripController(TripService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<TripResponse> findAllTrips() { return service.getAllTrips(); }

    @PostMapping
    ResponseEntity<String> saveNewTrip(@RequestBody @Valid TripRequest request) {
        service.createTrip(request);
        return new ResponseEntity<>("Voyage a été crée avec succès",HttpStatus.CREATED);
    }
}
