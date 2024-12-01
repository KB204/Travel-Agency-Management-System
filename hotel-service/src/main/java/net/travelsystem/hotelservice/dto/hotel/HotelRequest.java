package net.travelsystem.hotelservice.dto.hotel;

import jakarta.validation.constraints.NotEmpty;

public record HotelRequest(
        @NotEmpty(message = "Le nom de l'hotel est obligatoire")
        String name,
        @NotEmpty(message = "La localisation est obligatoire")
        String location,
        @NotEmpty(message = "Le contact est obligatoire")
        String contact) {}
