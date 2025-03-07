package net.travelsystem.paymentservice.dao;

import net.travelsystem.paymentservice.entities.Payment;
import net.travelsystem.paymentservice.entities.Refund;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class RefundRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        System.out.println("-----------------------------------------------------");

        Payment payment1 = Payment.builder().transactionIdentifier("test").build();
        Payment payment2 = Payment.builder().transactionIdentifier("test2").build();

        List<Payment> payments = List.of(payment1,payment2);

        paymentRepository.saveAll(payments);

        Refund refund1 = Refund.builder().refundAmount(5000).payment(payment1).build();
        Refund refund2 = Refund.builder().refundAmount(650).payment(payment2).build();

        List<Refund> refunds = List.of(refund1,refund2);

        refundRepository.saveAll(refunds);

        System.out.println("------------------------------------------------------");
    }

    @Test
    public void connectionEstablishedTest(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldFindRefundByPaymentIdentifier() {
        // given
        String paymentIdentifier = "test";

        // when
        Optional<Refund> refund = refundRepository.findByPayment_TransactionIdentifier(paymentIdentifier);

        // then
        assertThat(refund).isPresent();
    }

    @Test
    void shouldNotFindRefundByPaymentIdentifier() {
        // given
        String paymentIdentifier = "xx";

        // when
        Optional<Refund> refund = refundRepository.findByPayment_TransactionIdentifier(paymentIdentifier);

        // then
        assertThat(refund).isEmpty();
    }

}