package net.travelsystem.paymentservice.dao;

import net.travelsystem.paymentservice.entities.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund,Long> {
}
