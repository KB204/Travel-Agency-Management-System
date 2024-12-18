package net.travelsystem.paymentservice.dao;

import net.travelsystem.paymentservice.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findByTransactionIdentifier(String identifier);
}
