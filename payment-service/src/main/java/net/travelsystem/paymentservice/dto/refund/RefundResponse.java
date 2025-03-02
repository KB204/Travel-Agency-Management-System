package net.travelsystem.paymentservice.dto.refund;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.travelsystem.paymentservice.dto.payment.PaymentResponseDto;
import net.travelsystem.paymentservice.enums.RefundStatus;

import java.time.LocalDateTime;

public record RefundResponse(
        double refundAmount,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime processedAt,
        RefundStatus status,
        PaymentResponseDto payment) {}
