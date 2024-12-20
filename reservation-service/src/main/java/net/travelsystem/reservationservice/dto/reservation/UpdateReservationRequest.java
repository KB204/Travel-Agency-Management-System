package net.travelsystem.reservationservice.dto.reservation;

import jakarta.validation.constraints.NotNull;
import net.travelsystem.reservationservice.enums.ReservationStatus;

public record UpdateReservationRequest(@NotNull(message = "Le satatus est obligatoire") ReservationStatus status) {}
