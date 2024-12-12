package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.dto.payment.PaymentRequest;
import net.travelsystem.paymentservice.dto.payment.PaymentResponse;

import java.util.List;

public interface PaymentService {
    List<PaymentResponse> getAllPayments();
    void newPayment(PaymentRequest request);
}
