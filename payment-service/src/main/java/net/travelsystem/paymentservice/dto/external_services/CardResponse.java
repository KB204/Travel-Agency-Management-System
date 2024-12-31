package net.travelsystem.paymentservice.dto.external_services;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CardResponse(String cardNumber, String cardOwner, Double balance, LocalDate expirationDate, Integer verificationCode , String currency) {
}
