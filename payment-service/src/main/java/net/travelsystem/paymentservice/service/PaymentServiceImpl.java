package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.clients.CardRestClient;
import net.travelsystem.paymentservice.clients.ReservationRestClient;
import net.travelsystem.paymentservice.dao.PaymentRepository;
import net.travelsystem.paymentservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.paymentservice.dto.card.CardResponse;
import net.travelsystem.paymentservice.dto.payment.PaymentRequest;
import net.travelsystem.paymentservice.dto.payment.PaymentResponse;
import net.travelsystem.paymentservice.entities.Payment;
import net.travelsystem.paymentservice.enums.PaymentStatus;
import net.travelsystem.paymentservice.exceptions.PaymentException;
import net.travelsystem.paymentservice.mapper.PaymentMapper;
import net.travelsystem.paymentservice.service.specification.PaymentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final CardRestClient restClient;
    private final ReservationRestClient rest;
    private final PaymentMapper mapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, NotificationService notificationService, CardRestClient restClient, ReservationRestClient rest, PaymentMapper mapper) {
        this.paymentRepository = paymentRepository;
        this.notificationService = notificationService;
        this.restClient = restClient;
        this.rest = rest;
        this.mapper = mapper;
    }

    @Override
    public Page<PaymentResponse> getAllPayments(String identifier, String date, Double amount, String cardNumber, Pageable pageable) {

        Specification<Payment> specification = PaymentSpecification.filterWithoutConditions()
                .and(PaymentSpecification.paymentIdentifierEqual(identifier))
                .and(PaymentSpecification.paymentDateLike(date))
                .and(PaymentSpecification.paymentAmountEqual(amount))
                .and(PaymentSpecification.cardNumberEqual(cardNumber));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());

        return paymentRepository.findAll(specification,pageable)
                .map(payment -> {
                    payment.setCard(restClient.getCard(payment.getCardNumber()));
                    return mapper.paymentToDtoResponse(payment);
                });
    }

    @Override
    public void newPayment(String reservationIdentifier, PaymentRequest request) {
        CardResponse cardResponse = restClient.findCardByNumber(request.cardNumber());
        Double tripPrice = rest.getReservationTotalAmount(reservationIdentifier);

        Payment payment = Payment.builder()
                .transactionIdentifier(UUID.randomUUID().toString().substring(0,12))
                .createdAt(LocalDateTime.now())
                .status(PaymentStatus.COMPLETED)
                .amount(tripPrice)
                .currency(cardResponse.currency())
                .cardNumber(cardResponse.cardNumber())
                .reservationIdentifier(reservationIdentifier)
                .build();

        paymentRepository.findByTransactionIdentifier(payment.getTransactionIdentifier())
                        .ifPresent(a -> {
                            throw new ResourceAlreadyExists("Paiement exists déja");
                        });

        checkRules(payment,request);
        paymentRepository.save(payment);
        notificationService.debitCardEvent(payment.getReservationIdentifier(), payment.getAmount(), cardResponse.cardNumber());
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
