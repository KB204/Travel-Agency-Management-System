package net.travelsystem.paymentservice.dao;

import net.travelsystem.paymentservice.entities.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund,Long> {
    Optional<Refund> findByPayment_TransactionIdentifier(String identifier);
}
