package ma.emsi.volservice.dto.airline;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record AirlineRequest(
        @NotEmpty(message = "Le nom de compagnie aérienne est obligatoire ")
        String name,
        @NotEmpty(message = "Le code est obligatoire")
        String code,
        @NotEmpty(message = "Le siège social est obligatoire")
        String headquarters,
        @NotNull(message = "La capacite supporté par l'avion est obligatoire")
        Integer fleetSize) {}
