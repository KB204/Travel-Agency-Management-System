package net.travelsystem.hotelservice.dto.convention;

import net.travelsystem.hotelservice.dto.hotel.HotelResponseDTO;

import java.time.LocalDate;

public record ConventionResponse(
        String identifier,
        Integer availableRooms,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        HotelResponseDTO hotel) {}
