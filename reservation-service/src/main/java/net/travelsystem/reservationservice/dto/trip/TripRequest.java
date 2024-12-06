package net.travelsystem.reservationservice.dto.trip;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TripRequest(
        @NotEmpty(message = "L'identifiant de la convention avec l'hotel est obligatoire")
        String hotelConventionIdentifier,
        @NotEmpty(message = "L'identifiant de la convention avec la compagnie aérienne est obligatoire")
        String flightConventionIdentifier,
        @NotEmpty(message = "Le nom du voyage est obligatoire")
        String name,
        @NotEmpty(message = "La description est obligatoire")
        String description,
        @NotNull(message = "Le prix est obligatpire")
        @Min(value = 500,message = "Le prix ne peut pas être inférieur a 500")
        Double price,
        @NotEmpty(message = "Le point de départ est obligatoire")
        String origin,
        @NotEmpty(message = "La destination est obligatoire")
        String destination,
        @NotNull(message = "La durée du voyage est obligatoire")
        @Min(value = 2,message = "La durée ne peut pas être inférieure a 2 Jours")
        Integer duration,
        @NotNull(message = "Le nombre des places disponbiles est obligatoire")
        Integer availablePlaces
) {}
