package net.travelsystem.reservationservice.dto.client;

import net.travelsystem.reservationservice.dto.reservation.ReservationResponseDTO;

import java.util.List;

public record ClientResponseDetails(String identity, String firstName, String lastName, List<ReservationResponseDTO> reservations) {}
