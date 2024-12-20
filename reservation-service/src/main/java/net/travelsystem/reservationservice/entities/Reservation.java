package net.travelsystem.reservationservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travelsystem.reservationservice.dto.external_services.FlightConventionDTO;
import net.travelsystem.reservationservice.dto.external_services.HotelConventionDTO;
import net.travelsystem.reservationservice.enums.ReservationStatus;

import java.time.LocalDate;
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
    private LocalDateTime updatedAt;
    private Double totalPrice;
    private LocalDateTime flightDepartureTime;
    private String departureLocation;
    private LocalDate returnDate;
    private String hotelName;
    private String airlineName;
    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;
    @Transient
    private HotelConventionDTO hotelConvention;
    @Transient
    private FlightConventionDTO flightConvention;
}
