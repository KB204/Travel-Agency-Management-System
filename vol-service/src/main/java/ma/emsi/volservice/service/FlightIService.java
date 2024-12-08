package ma.emsi.volservice.service;

import ma.emsi.volservice.dto.flight.FlightRequest;
import ma.emsi.volservice.dto.flight.FlightResponse;

import java.util.List;

public interface FlightIService {
    List<FlightResponse> getAllFlights();
    void addFlight(FlightRequest flightRequest);
}
