package net.travelsystem.reservationservice.dto.reservation;

import net.travelsystem.reservationservice.dto.trip.TripResponseDetails;
import net.travelsystem.reservationservice.enums.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponseDTO(
        String identifier,
        ReservationStatus status,
        LocalDateTime reservationDate,
        Double totalPrice,
        Integer nbrTickets,
        TripResponseDetails trip) {}
