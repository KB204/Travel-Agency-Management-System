package net.travelsystem.paymentservice.dto.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PaymentRequest(
        @NotEmpty(message = "Le numéro de la carte bancaire est obligatoire")
        @Size(min = 16,message = "La carte n'est pas valide")
        @Size(max = 16,message = "La carte n'est pas valide")
        String cardNumber,
        @NotEmpty(message = "Le nom du propriétaire de la carte bancaire est obligatoire")
        String cardOwner,
        @NotNull(message = "La date d'expiration de la carte est obligatoire")
        LocalDate expirationDate,
        @NotNull(message = "Le code de la carte est obligatoire")
        @Min(value = 3,message = "Le code n'est pas valide")
        Integer verificationCode) {}
