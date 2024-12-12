package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.clients.CardRestClient;
import net.travelsystem.paymentservice.dao.PaymentRepository;
import net.travelsystem.paymentservice.dto.card.CardResponse;
import net.travelsystem.paymentservice.dto.payment.PaymentRequest;
import net.travelsystem.paymentservice.dto.payment.PaymentResponse;
import net.travelsystem.paymentservice.entities.Payment;
import net.travelsystem.paymentservice.enums.PaymentStatus;
import net.travelsystem.paymentservice.mapper.PaymentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CardRestClient restClient;
    private final PaymentMapper mapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, CardRestClient restClient, PaymentMapper mapper) {
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(payment -> {
                    payment.setCard(restClient.findCardByNumber(payment.getCardNumber()));
                    return mapper.paymentToDtoResponse(payment);
                })
                .toList();
    }

    @Override
    public void newPayment(PaymentRequest request) {
        CardResponse cardResponse = restClient.findCardByNumber(request.cardNumber());

        Payment payment = Payment.builder()
                .transactionIdentifier(UUID.randomUUID().toString().substring(0,12))
                .createdAt(LocalDateTime.now())
                .status(PaymentStatus.COMPLETED)
                .amount(request.amount())
                .currency(cardResponse.currency())
                .cardNumber(cardResponse.cardNumber())
                .build();

        paymentRepository.save(payment);
    }
}
