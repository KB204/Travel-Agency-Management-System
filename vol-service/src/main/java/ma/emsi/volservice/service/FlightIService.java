package ma.emsi.volservice.service;

import ma.emsi.volservice.dto.flight.FlightRequest;
import ma.emsi.volservice.dto.flight.FlightResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FlightIService {
    Page<FlightResponse> getAllFlights(String flightNo, String type, String origin, String destination, String airline, String depDate, Pageable pageable);
    void addFlight(FlightRequest flightRequest);
}
