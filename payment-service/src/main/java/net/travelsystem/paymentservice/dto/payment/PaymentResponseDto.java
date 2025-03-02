package net.travelsystem.paymentservice.dto.payment;

import java.time.LocalDateTime;

public record PaymentResponseDto(
        String transactionIdentifier,
        LocalDateTime createdAt,
        Double amount,
        String currency) {}
