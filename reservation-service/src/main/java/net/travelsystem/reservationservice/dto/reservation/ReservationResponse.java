package net.travelsystem.reservationservice.dto.reservation;

import net.travelsystem.reservationservice.dto.client.ClientResponseDTO;
import net.travelsystem.reservationservice.dto.external_services.FlightConventionDTO;
import net.travelsystem.reservationservice.dto.external_services.HotelConventionDTO;
import net.travelsystem.reservationservice.dto.trip.TripResponseDTO;
import net.travelsystem.reservationservice.enums.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        String identifier,
        ReservationStatus status,
        LocalDateTime reservationDate,
        LocalDateTime updatedAt,
        Double totalPrice,
        ClientResponseDTO client,
        TripResponseDTO trip,
        HotelConventionDTO hotelConvention,
        FlightConventionDTO flightConvention) {}
