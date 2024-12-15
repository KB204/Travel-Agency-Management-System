package net.travelsystem.cardserver.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRequest(
        @NotEmpty(message = "Le numéro de la carte bancaire est obligatoire")
        @Size(min = 16,message = "La carte n'est pas valide")
        @Size(max = 16,message = "La carte n'est pas valide")
        String cardNumber,
        @NotNull(message = "Le solde est obligatoire")
        @Min(value = 100,message = "Le solde ne peut pas etre inférieur a 100")
        Double amount) {}
