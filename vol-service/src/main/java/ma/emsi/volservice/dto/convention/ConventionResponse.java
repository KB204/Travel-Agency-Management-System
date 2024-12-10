package ma.emsi.volservice.dto.convention;

import ma.emsi.volservice.dto.flight.FlightResponse;

public record ConventionResponse(Integer availablePlaces, FlightResponse flight) {}
