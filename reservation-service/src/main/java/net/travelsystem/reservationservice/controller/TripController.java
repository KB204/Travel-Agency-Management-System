package net.travelsystem.reservationservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.reservationservice.dto.trip.TripRequest;
import net.travelsystem.reservationservice.dto.trip.TripResponse;
import net.travelsystem.reservationservice.dto.trip.TripUpdateRequest;
import net.travelsystem.reservationservice.service.TripService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "http://localhost:4200")
public class TripController {
    private final TripService service;

    public TripController(TripService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<TripResponse> findAllTrips(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String identifier,
            Pageable pageable) {
        return service.getAllTrips(name, price, destination, identifier, pageable); }

    @PostMapping
    ResponseEntity<String> saveNewTrip(@RequestBody @Valid TripRequest request) {
        service.createTrip(request);
        return new ResponseEntity<>("Voyage a été crée avec succès",HttpStatus.CREATED);
    }
    @PutMapping("/{id}/updateTrip")
    ResponseEntity<String> updateExistingTrip(@PathVariable Long id,@RequestBody @Valid TripUpdateRequest request) {
        service.updateTrip(id, request);
        return new ResponseEntity<>("Voyage a été modifié avec succès",HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<TripResponse> removeTripById(@PathVariable Long id) {
        service.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
