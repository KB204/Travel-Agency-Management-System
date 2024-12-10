package net.travelsystem.reservationservice.dto.external_services;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FlightResponse(
        String flightNo,
        String origin,
        String destination,
        String flightType,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        AirlineResponseDTO airline) {}
