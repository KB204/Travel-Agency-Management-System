package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.clients.CardRestClient;
import net.travelsystem.paymentservice.clients.TripRestClient;
import net.travelsystem.paymentservice.dao.PaymentRepository;
import net.travelsystem.paymentservice.dto.ResourceAlreadyExists;
import net.travelsystem.paymentservice.dto.card.CardResponse;
import net.travelsystem.paymentservice.dto.payment.PaymentRequest;
import net.travelsystem.paymentservice.dto.payment.PaymentResponse;
import net.travelsystem.paymentservice.entities.Payment;
import net.travelsystem.paymentservice.enums.PaymentStatus;
import net.travelsystem.paymentservice.exceptions.PaymentException;
import net.travelsystem.paymentservice.mapper.PaymentMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final CardRestClient restClient;
    private final TripRestClient rest;
    private final PaymentMapper mapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, NotificationService notificationService, CardRestClient restClient, TripRestClient rest, PaymentMapper mapper) {
        this.paymentRepository = paymentRepository;
        this.notificationService = notificationService;
        this.restClient = restClient;
        this.rest = rest;
        this.mapper = mapper;
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(payment -> {
                    payment.setCard(restClient.getCard(payment.getCardNumber()));
                    return mapper.paymentToDtoResponse(payment);
                })
                .toList();
    }

    @Override
    public void newPayment(Long id, PaymentRequest request) {
        CardResponse cardResponse = restClient.findCardByNumber(request.cardNumber());
        Double tripPrice = rest.getTripPrice(id);

        Payment payment = Payment.builder()
                .transactionIdentifier(UUID.randomUUID().toString().substring(0,12))
                .createdAt(LocalDateTime.now())
                .status(PaymentStatus.COMPLETED)
                .amount(tripPrice)
                .currency(cardResponse.currency())
                .cardNumber(cardResponse.cardNumber())
                .build();

        paymentRepository.findByTransactionIdentifier(payment.getTransactionIdentifier())
                        .ifPresent(a -> {
                            throw new ResourceAlreadyExists("Paiement exists déja");
                        });

        checkRules(payment,request);
        paymentRepository.save(payment);
        notificationService.debitCardEvent(id, tripPrice, cardResponse.cardNumber());
    }

    private void checkRules(Payment payment,PaymentRequest request){
        if (payment.getAmount() > restClient.findCardByNumber(payment.getCardNumber()).balance())
            throw new PaymentException("Le solde du compte n’est pas suffisant pour effectuer cette opération");
        if (!request.cardOwner().equals(restClient.findCardByNumber(payment.getCardNumber()).cardOwner()))
            throw new PaymentException("Carte invalide");
        if (!request.expirationDate().equals(restClient.findCardByNumber(payment.getCardNumber()).expirationDate()))
            throw new PaymentException("Carte invalide");
        if (!request.verificationCode().equals(restClient.findCardByNumber(payment.getCardNumber()).verificationCode()))
            throw new PaymentException("Carte invalide");
    }
}
