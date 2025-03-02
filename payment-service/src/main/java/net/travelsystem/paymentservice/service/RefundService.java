package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.dto.refund.RefundRequest;
import net.travelsystem.paymentservice.dto.refund.RefundResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RefundService {
    Page<RefundResponse> findAllRefunds(String identifier, Double amount, String date, String status, Pageable pageable);
    void saveNewRefund(String transactionIdentifier);
    void declineRefund(String transactionIdentifier);
    void acceptRefund(String transactionIdentifier, RefundRequest request);
}
