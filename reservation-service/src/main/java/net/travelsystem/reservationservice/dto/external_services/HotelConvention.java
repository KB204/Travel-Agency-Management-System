package net.travelsystem.reservationservice.dto.external_services;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record HotelConvention(
        String identifier,
        Integer availableRooms,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        HotelResponseDTO hotel) {}
