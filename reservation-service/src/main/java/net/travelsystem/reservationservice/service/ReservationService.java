package net.travelsystem.reservationservice.service;

import net.travelsystem.paymentservice.dto.event.PaymentEvent;
import net.travelsystem.reservationservice.dto.reservation.ReservationRequest;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.dto.reservation.UpdateReservationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ReservationService {
    Page<ReservationResponse> getAllReservations(String identifier, String status, Double amount, String date, String identity, String lastname, String firstname, Pageable pageable);
    void createNewReservation(Long tripId, ReservationRequest request);
    void completeReservation(PaymentEvent event);
    void updateReservation(String identifier, UpdateReservationRequest request);
    Double reservationTotalAmount(String identifier);
}
