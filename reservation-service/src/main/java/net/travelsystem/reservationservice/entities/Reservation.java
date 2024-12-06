package net.travelsystem.reservationservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travelsystem.reservationservice.enums.ReservationStatus;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identifier;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private LocalDateTime reservationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;
}
