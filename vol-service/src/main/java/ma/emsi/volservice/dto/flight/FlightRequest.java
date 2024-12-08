package ma.emsi.volservice.dto.flight;

import jakarta.validation.constraints.NotEmpty;
import ma.emsi.volservice.model.Airline;

import java.time.LocalDateTime;

public record FlightRequest(
        @NotEmpty(message = "Le numero de vol est obligatoire")
        String flightNo,
        @NotEmpty(message = "L'origine de vol est obligatoire")
        String origin,
        @NotEmpty(message = "La destination de vol est obligatoire")
        String destination,
        @NotEmpty(message = "La date de vol est obligatoire")
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        @NotEmpty(message = "Le nom de compagnie aérienne est obligatoire")
        Airline airline
) {}
