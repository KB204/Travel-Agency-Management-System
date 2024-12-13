package net.travelsystem.paymentservice.dto.event;

public record PaymentEvent(Long TripId,Double amount,String cardNumber) {}
