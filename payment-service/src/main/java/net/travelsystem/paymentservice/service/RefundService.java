package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.dto.refund.RefundRequest;
import net.travelsystem.paymentservice.dto.refund.RefundResponse;

import java.util.List;

public interface RefundService {
    List<RefundResponse> findAllRefunds();
    void saveNewRefund(String transactionIdentifier);
    void declineRefund(String transactionIdentifier);
    void acceptRefund(String transactionIdentifier, RefundRequest request);
}
