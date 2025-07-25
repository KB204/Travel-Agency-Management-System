package net.travelsystem.paymentservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travelsystem.paymentservice.dto.external_services.CardResponse;
import net.travelsystem.paymentservice.enums.PaymentStatus;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionIdentifier;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double amount;
    private String currency;
    private String cardNumber;
    private String reservationIdentifier;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Transient
    private CardResponse card;
}
