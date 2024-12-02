package net.travelsystem.hotelservice.dto.convention;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConventionRequest(
        @NotEmpty(message = "L'hotel est obligaoire")
        String name,
        @NotEmpty(message = "La localisation de l'hotel est obligatoire")
        String location,
        @NotNull(message = "La date de l'entrée est obligtaoire")
        LocalDate checkInDate,
        @NotNull(message = "La date de la sortie est obligatoire")
        LocalDate checkOutDate) {}
