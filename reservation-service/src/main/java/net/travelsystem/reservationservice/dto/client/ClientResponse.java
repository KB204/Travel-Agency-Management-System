package net.travelsystem.reservationservice.dto.client;

public record ClientResponse(
        String identity,
        String passportNumber,
        String firstName,
        String lastName,
        String email,
        String phoneNumber) {}
