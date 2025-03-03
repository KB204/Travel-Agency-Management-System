package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.dao.PaymentRepository;
import net.travelsystem.paymentservice.dao.RefundRepository;
import net.travelsystem.paymentservice.dto.refund.RefundRequest;
import net.travelsystem.paymentservice.dto.refund.RefundResponse;
import net.travelsystem.paymentservice.entities.Refund;
import net.travelsystem.paymentservice.enums.PaymentStatus;
import net.travelsystem.paymentservice.enums.RefundStatus;
import net.travelsystem.paymentservice.exceptions.PaymentException;
import net.travelsystem.paymentservice.exceptions.ResourceNotFoundException;
import net.travelsystem.paymentservice.mapper.RefundMapper;
import net.travelsystem.paymentservice.service.specification.RefundSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static java.lang.String.format;

@Service
@Transactional
public class RefundServiceImpl implements RefundService {
    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final RefundMapper mapper;

    public RefundServiceImpl(RefundRepository refundRepository, PaymentRepository paymentRepository, RefundMapper mapper) {
        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<RefundResponse> findAllRefunds(String identifier, Double amount, String date, String status, Pageable pageable) {

        Specification<Refund> specification = RefundSpecification.filterWithoutConditions()
                .and(RefundSpecification.refundIdentifierEqual(identifier))
                .and(RefundSpecification.refundAmountEqual(amount))
                .and(RefundSpecification.refundDateLike(date))
                .and(RefundSpecification.refundStatusEqual(status));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("processedAt").descending());

        return refundRepository.findAll(specification,pageable)
                .map(mapper::refundToDtoResponse);
    }

    @Override
    public void saveNewRefund(String transactionIdentifier) {
        var payment = paymentRepository.findByTransactionIdentifier(transactionIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("Le paiement identifié par %s n'existe pas",transactionIdentifier)));

        if (payment.getStatus().equals(PaymentStatus.COMPLETED)){
            throw new PaymentException(format("Le paiement identifié par %s ne peut pas être remboursé",transactionIdentifier));
        }

        Refund refund = Refund.builder()
                .processedAt(LocalDateTime.now())
                .payment(payment)
                .status(RefundStatus.PROGRESS)
                .build();

        refundRepository.save(refund);
    }

    @Override
    public void declineRefund(String transactionIdentifier) {
        var refund = refundRepository.findByPayment_TransactionIdentifier(transactionIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("Le remboursement identifié par %s n'existe pas",transactionIdentifier)));

        refund.setStatus(RefundStatus.DECLINED);
        refundRepository.save(refund);
    }

    @Override
    public void acceptRefund(String transactionIdentifier, RefundRequest request) {
        var refund = refundRepository.findByPayment_TransactionIdentifier(transactionIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("Le remboursement identifié par %s n'existe pas",transactionIdentifier)));

        refund.setRefundAmount(request.refundAmount());
        refund.setStatus(RefundStatus.ACCEPTED);
        refund.getPayment().setStatus(PaymentStatus.REFUNDED);

        refundRepository.save(refund);
    }
}
