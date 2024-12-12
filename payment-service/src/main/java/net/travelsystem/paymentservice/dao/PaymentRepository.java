package net.travelsystem.paymentservice.dao;

import net.travelsystem.paymentservice.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByTransactionIdentifier(String identifier);
}
