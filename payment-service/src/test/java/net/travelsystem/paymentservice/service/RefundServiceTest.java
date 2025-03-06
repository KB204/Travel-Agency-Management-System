package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.dao.PaymentRepository;
import net.travelsystem.paymentservice.dao.RefundRepository;
import net.travelsystem.paymentservice.dto.refund.RefundRequest;
import net.travelsystem.paymentservice.entities.Payment;
import net.travelsystem.paymentservice.entities.Refund;
import net.travelsystem.paymentservice.enums.PaymentStatus;
import net.travelsystem.paymentservice.enums.RefundStatus;
import net.travelsystem.paymentservice.exceptions.PaymentException;
import net.travelsystem.paymentservice.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefundServiceTest {
    @Mock
    private RefundRepository refundRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private RefundServiceImpl underTest;

    @Test
    void shouldSaveNewRefund() {
        // given
        Payment payment = Payment.builder()
                .transactionIdentifier("test")
                .status(PaymentStatus.PENDING)
                .amount(5000.0)
                .currency("MAD")
                .build();

        when(paymentRepository.findByTransactionIdentifier(payment.getTransactionIdentifier())).thenReturn(Optional.of(payment));

        // when
        underTest.saveNewRefund(payment.getTransactionIdentifier());

        // then
        ArgumentCaptor<Refund> captor = ArgumentCaptor.forClass(Refund.class);
        verify(refundRepository).save(captor.capture());

        Refund savedRefund = captor.getValue();
        assertThat(savedRefund).isNotNull();
        assertThat(savedRefund.getPayment()).isEqualTo(payment);
        assertThat(savedRefund.getPayment().getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void shouldNotCreateNewRefundWhenPaymentDoesntExist() {
        // given
        String paymentIdentifier = "test";

        // when
        when(paymentRepository.findByTransactionIdentifier(paymentIdentifier)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.saveNewRefund(paymentIdentifier))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(refundRepository,never()).save(any(Refund.class));
    }

    @Test
    void shouldNotCreateNewRefundWhenPaymentIsCompleted() {
        // given
        Payment payment = Payment.builder()
                .transactionIdentifier("test")
                .status(PaymentStatus.COMPLETED)
                .amount(5000.0)
                .currency("MAD")
                .build();
        // when
        when(paymentRepository.findByTransactionIdentifier(payment.getTransactionIdentifier())).thenReturn(Optional.of(payment));

        // then
        assertThatThrownBy(() -> underTest.saveNewRefund(payment.getTransactionIdentifier()))
                .isInstanceOf(PaymentException.class);
        verify(refundRepository,never()).save(any(Refund.class));
    }

    @Test
    void shouldDeclineRefund() {
        // given
        String paymentIdentifier = "test";
        Refund refund = Refund.builder()
                .status(RefundStatus.DECLINED)
                .processedAt(LocalDateTime.now())
                .build();

        when(refundRepository.findByPayment_TransactionIdentifier(paymentIdentifier)).thenReturn(Optional.of(refund));

        // when
        underTest.declineRefund(paymentIdentifier);

        // then
        assertThat(refund.getStatus()).isEqualTo(RefundStatus.DECLINED);
        verify(refundRepository).save(refund);
    }

    @Test
    void shouldNotDeclineRefund() {
        // given
        String paymentIdentifier = "test";

        // when
        when(refundRepository.findByPayment_TransactionIdentifier(paymentIdentifier)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.declineRefund(paymentIdentifier))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(refundRepository,never()).save(any(Refund.class));
    }

    @Test
    void shouldAcceptRefund() {
        // given
        String paymentIdentifier = "test";
        RefundRequest request = new RefundRequest(5000.0);
        Refund refund = Refund.builder()
                .status(RefundStatus.ACCEPTED)
                .refundAmount(request.refundAmount())
                .payment(Payment.builder().status(PaymentStatus.REFUNDED).build())
                .build();

        when(refundRepository.findByPayment_TransactionIdentifier(paymentIdentifier)).thenReturn(Optional.of(refund));

        // when
        underTest.acceptRefund(paymentIdentifier,request);

        // then
        assertThat(refund.getStatus()).isEqualTo(RefundStatus.ACCEPTED);
        assertThat(refund.getRefundAmount()).isEqualTo(request.refundAmount());
        assertThat(refund.getPayment().getStatus()).isEqualTo(PaymentStatus.REFUNDED);
        verify(refundRepository).save(refund);
    }

    @Test
    void shouldNotAcceptRefund() {
        // given
        String paymentIdentifier = "test";
        RefundRequest request = new RefundRequest(5000.0);

        // when
        when(refundRepository.findByPayment_TransactionIdentifier(paymentIdentifier)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.acceptRefund(paymentIdentifier,request))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(refundRepository,never()).save(any(Refund.class));
    }
}