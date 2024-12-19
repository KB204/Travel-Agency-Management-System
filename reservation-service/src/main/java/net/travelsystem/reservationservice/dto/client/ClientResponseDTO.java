package net.travelsystem.reservationservice.dto.client;

public record ClientResponseDTO(
        String identity,
        String passportNumber,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Integer nbrTickets) {}
