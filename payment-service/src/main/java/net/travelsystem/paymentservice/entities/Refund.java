package net.travelsystem.paymentservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travelsystem.paymentservice.enums.RefundStatus;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double refundAmount;
    private LocalDateTime processedAt;
    @Enumerated(EnumType.STRING)
    private RefundStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;
}
