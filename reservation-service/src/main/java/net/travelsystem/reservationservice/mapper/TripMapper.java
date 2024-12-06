package net.travelsystem.reservationservice.mapper;

import net.travelsystem.reservationservice.dto.trip.TripRequest;
import net.travelsystem.reservationservice.dto.trip.TripResponse;
import net.travelsystem.reservationservice.entities.Trip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripMapper {
    TripResponse tripToDtoResponse(Trip trip);
    Trip dtoRequestToTrip(TripRequest request);
}
