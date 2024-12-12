package net.travelsystem.cardserver.dto;

import java.time.LocalDate;

public record CardResponse(String cardNumber, String cardOwner, Double balance, LocalDate expirationDate, Integer verificationCode , String currency) {}
