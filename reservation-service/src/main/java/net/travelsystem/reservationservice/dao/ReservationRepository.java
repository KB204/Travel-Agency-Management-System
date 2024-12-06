package net.travelsystem.reservationservice.dao;

import net.travelsystem.reservationservice.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
