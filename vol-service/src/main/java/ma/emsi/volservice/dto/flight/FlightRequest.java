package ma.emsi.volservice.dto.flight;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ma.emsi.volservice.enums.FlightType;

import java.time.LocalDateTime;

public record FlightRequest(
        @NotEmpty(message = "Le nom de airline est obligatoire")
        String name,
        @NotEmpty(message = "Le icao est obligatoire")
        String code,
        @NotEmpty(message = "L'origine de vol est obligatoire")
        String origin,
        @NotEmpty(message = "La destination de vol est obligatoire")
        String destination,
        @NotNull(message = "le type de vol est obligatoire")
        FlightType flightType,
        @NotNull(message = "La date de départ du vol est obligatoire")
        LocalDateTime departureTime,
        @NotNull(message = "La date d'arrivée est obligatoire")
        LocalDateTime arrivalTime) {}
