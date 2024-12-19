package net.travelsystem.reservationservice.dto.trip;


public record TripResponseDTO(
        String name,
        String description,
        Double price,
        String origin,
        String destination,
        Integer duration) {}
