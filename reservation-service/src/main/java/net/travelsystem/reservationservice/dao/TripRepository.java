package net.travelsystem.reservationservice.dao;

import net.travelsystem.reservationservice.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip,Long> {
    Optional<Trip> findByHotelConventionIdentifierOrFlightConventionIdentifier(String hotelIdentifier,String flightIdentifier);
}
