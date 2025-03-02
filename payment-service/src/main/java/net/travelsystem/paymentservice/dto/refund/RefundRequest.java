package net.travelsystem.paymentservice.dto.refund;

import jakarta.validation.constraints.Min;

public record RefundRequest(
        @Min(value = 0, message = "Veuillez entrer un montant valide")
        double refundAmount) {}
