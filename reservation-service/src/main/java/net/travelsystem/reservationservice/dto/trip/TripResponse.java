package net.travelsystem.reservationservice.dto.trip;

import net.travelsystem.reservationservice.dto.external_services.FlightConvention;
import net.travelsystem.reservationservice.dto.external_services.HotelConvention;

public record TripResponse(
        Long id,
        String name,
        String description,
        Double price,
        String origin,
        String destination,
        Integer duration,
        Integer availablePlaces,
        HotelConvention hotel,
        FlightConvention flight) {}
