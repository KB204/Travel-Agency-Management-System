package ma.emsi.volservice.dto.convention;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ConventionRequest(
        @NotEmpty(message = "L'identifiant du vol est obligatoire")
        String flightNo,
        @NotNull(message = "Le nombre des places disponibles est obligatoire")
        Integer availablePlaces) {}
