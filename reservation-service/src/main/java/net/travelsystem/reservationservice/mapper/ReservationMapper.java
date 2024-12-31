package net.travelsystem.reservationservice.mapper;

import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponseDTO;
import net.travelsystem.reservationservice.entities.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationResponse reservationToDtoResponse(Reservation reservation);
    ReservationResponseDTO reservationToDtoDetails(Reservation reservation);
}
