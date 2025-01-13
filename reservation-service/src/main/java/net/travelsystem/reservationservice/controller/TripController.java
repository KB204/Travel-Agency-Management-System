package net.travelsystem.reservationservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.reservationservice.dto.trip.TripDetailsDTO;
import net.travelsystem.reservationservice.dto.trip.TripRequest;
import net.travelsystem.reservationservice.dto.trip.TripResponse;
import net.travelsystem.reservationservice.dto.trip.TripUpdateRequest;
import net.travelsystem.reservationservice.service.TripService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final TripService service;

    public TripController(TripService service) {
        this.service = service;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    Page<TripResponse> findAllTrips(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String identifier,
            Pageable pageable) {
        return service.getAllTrips(name, price, destination, identifier, pageable); }

    @GetMapping("/tripsDetails")
    ResponseEntity<TripDetailsDTO> tripsDetails() {
        TripDetailsDTO tripDetailsDTO = service.tripReservationsDetails();
        return new ResponseEntity<>(tripDetailsDTO,HttpStatus.OK);
    }

    @PostMapping("/newTrip")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<String> saveNewTrip(@RequestBody @Valid TripRequest request) {
        service.createTrip(request);
        return new ResponseEntity<>("Voyage a été crée avec succès",HttpStatus.CREATED);
    }
    @PutMapping("/{id}/updateTrip")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<String> updateExistingTrip(@PathVariable Long id,@RequestBody @Valid TripUpdateRequest request) {
        service.updateTrip(id, request);
        return new ResponseEntity<>("Voyage a été modifié avec succès",HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<TripResponse> removeTripById(@PathVariable Long id) {
        service.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
