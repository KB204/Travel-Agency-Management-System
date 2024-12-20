package net.travelsystem.reservationservice.service;

import net.travelsystem.paymentservice.dto.event.PaymentEvent;
import net.travelsystem.reservationservice.dto.reservation.ReservationRequest;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.dto.reservation.UpdateReservationRequest;

import java.util.List;

public interface ReservationService {
    List<ReservationResponse> getAllReservations();
    void createNewReservation(Long tripId, ReservationRequest request);
    void completeReservation(PaymentEvent event);
    void updateReservation(String identifier, UpdateReservationRequest request);
    Double reservationTotalAmount(String identifier);
}
