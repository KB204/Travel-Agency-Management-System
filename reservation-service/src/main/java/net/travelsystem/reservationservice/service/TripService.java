package net.travelsystem.reservationservice.service;

import net.travelsystem.reservationservice.dto.trip.TripRequest;
import net.travelsystem.reservationservice.dto.trip.TripResponse;

import java.util.List;

public interface TripService {
    List<TripResponse> getAllTrips();
    void createTrip(TripRequest request);
    void updateTrip(TripRequest request);
    void deleteTrip(Long id);
}
