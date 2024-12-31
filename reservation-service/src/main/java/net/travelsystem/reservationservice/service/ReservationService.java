package net.travelsystem.reservationservice.service;

import net.travelsystem.paymentservice.dto.event.PaymentEvent;
import net.travelsystem.reservationservice.dto.client.ClientResponseDetails;
import net.travelsystem.reservationservice.dto.reservation.ClientReservationRequest;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.dto.reservation.UpdateReservationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ReservationService {
    Page<ReservationResponse> getAllReservations(String identifier, String status, Double amount, String date, String identity, String lastname, String firstname, Pageable pageable);
    void createNewReservation(Long tripId, ClientReservationRequest request);
    void completeReservation(PaymentEvent event);
    void updateReservation(String identifier, UpdateReservationRequest request);
    ClientResponseDetails getClientReservations(String identity, String status, Double amount, String date, Pageable pageable);
    Double reservationTotalAmount(String identifier);
    void deleteReservation(String identifier);
}
