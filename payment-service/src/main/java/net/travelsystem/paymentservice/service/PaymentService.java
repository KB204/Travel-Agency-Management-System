package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.dto.payment.PaymentRequest;
import net.travelsystem.paymentservice.dto.payment.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PaymentService {
    Page<PaymentResponse> getAllPayments(String identifier, String date, Double amount, String cardNumber, Pageable pageable);
    void newPayment(String reservationIdentifier,PaymentRequest request);
}
