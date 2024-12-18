package net.travelsystem.paymentservice.dto.event;

public record PaymentEvent(Long tripId,Double amount,String cardNumber) {}
