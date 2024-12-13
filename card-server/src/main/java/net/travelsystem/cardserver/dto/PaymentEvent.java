package net.travelsystem.cardserver.dto;

public record PaymentEvent(Long TripId, Double amount,String cardNumber) {}
