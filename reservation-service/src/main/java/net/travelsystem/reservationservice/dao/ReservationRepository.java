package net.travelsystem.reservationservice.dao;

import net.travelsystem.reservationservice.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Optional<Reservation> findByIdentifierIgnoreCase(String identifier);
}
