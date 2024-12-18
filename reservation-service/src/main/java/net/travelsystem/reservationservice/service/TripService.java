package net.travelsystem.reservationservice.service;

import net.travelsystem.paymentservice.dto.event.PaymentEvent;
import net.travelsystem.reservationservice.dto.trip.TripRequest;
import net.travelsystem.reservationservice.dto.trip.TripResponse;
import net.travelsystem.reservationservice.dto.trip.TripUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TripService {
    Page<TripResponse> getAllTrips(String name, Double price, String destination, String identifier, Pageable pageable);
    void createTrip(TripRequest request);
    void updateTrip(Long id, TripUpdateRequest request);
    void updateReservedTrip(PaymentEvent paymentEvent);
    Double getTripPrice(Long id);
    void deleteTrip(Long id);
}
