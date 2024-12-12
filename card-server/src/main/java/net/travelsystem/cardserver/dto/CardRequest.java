package net.travelsystem.cardserver.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CardRequest(
        @NotEmpty(message = "Le numéro de la carte bancaire est obligatoire")
        @Size(min = 16,message = "La carte n'est pas valide")
        @Size(max = 16,message = "La carte n'est pas valide")
        String cardNumber,
        @NotEmpty(message = "Le nom du propriétaire de la carte bancaire est obligatoire")
        String cardOwner,
        @NotNull(message = "Le solde est obligatoire")
        @Min(value = 100,message = "Le solde ne peut pas etre inférieur a 100")
        Double balance,
        @NotNull(message = "La date d'expiration de la carte est obligatoire")
        LocalDate expirationDate,
        @NotNull(message = "Le code de la carte est obligatoire")
        @Min(value = 3,message = "Le code n'est pas valide")
        Integer verificationCode,
        @NotEmpty(message = "Le devise est obligatoire")
        String currency) {}
