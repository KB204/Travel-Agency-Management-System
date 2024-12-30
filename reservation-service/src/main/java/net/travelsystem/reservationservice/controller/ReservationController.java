package net.travelsystem.reservationservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.reservationservice.dto.client.ClientResponseDetails;
import net.travelsystem.reservationservice.dto.reservation.ReservationRequest;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.dto.reservation.UpdateReservationRequest;
import net.travelsystem.reservationservice.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static java.lang.String.format;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ReservationResponse> findAllReservations(
            @RequestParam(required = false) String identifier,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String identity,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String firstname,
            Pageable pageable) {
        return service.getAllReservations(identifier, status, amount, date, identity, lastname, firstname, pageable); }

    @GetMapping("/{identifier}/reservation/totalPrice")
    ResponseEntity<Double> findReservationDetails(@PathVariable String identifier) {
        Double reservationTotalAmount = service.reservationTotalAmount(identifier);
        return new ResponseEntity<>(reservationTotalAmount,HttpStatus.OK);
    }

    @GetMapping("/{identity}/client/reservationsDetails")
    ResponseEntity<ClientResponseDetails> findAllClientReservations(
            @PathVariable String identity,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) String date,
            Pageable pageable) {

        ClientResponseDetails clientReservations = service.getClientReservations(identity, status, amount, date, pageable);
        return new ResponseEntity<>(clientReservations,HttpStatus.OK);
    }

    @PostMapping("/newReservation")
    ResponseEntity<String> saveNewReservation(@RequestParam Long tripId, @RequestBody @Valid ReservationRequest request) {
        service.createNewReservation(tripId, request);
        return new ResponseEntity<>(format("La reservation pour le client identifié par %s est en cours",request.identity()),HttpStatus.CREATED);
    }

    @PutMapping("/{identifier}/reservation/updateStatus")
    ResponseEntity<String> editReservationStatus(@PathVariable String identifier, @RequestBody @Valid UpdateReservationRequest request) {
        service.updateReservation(identifier, request);
        return new ResponseEntity<>(format("La reservation numéro %s a été modifiée avec succès",identifier),HttpStatus.ACCEPTED);
    }
}
