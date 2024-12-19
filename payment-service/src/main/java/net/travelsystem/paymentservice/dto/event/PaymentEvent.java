package net.travelsystem.paymentservice.dto.event;

public record PaymentEvent(String reservationIdentifier,Double amount,String cardNumber) {}
