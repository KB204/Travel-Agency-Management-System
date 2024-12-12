package net.travelsystem.paymentservice.dto.payment;

import net.travelsystem.paymentservice.dto.card.CardResponse;
import net.travelsystem.paymentservice.enums.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(
        String transactionIdentifier,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Double amount,
        String currency,
        PaymentStatus status,
        CardResponse card) {}
