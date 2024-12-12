package net.travelsystem.paymentservice.dto.card;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CardResponse(String cardNumber, String cardOwner, Double balance, LocalDate expirationDate, Integer verificationCode , String currency) {
}
