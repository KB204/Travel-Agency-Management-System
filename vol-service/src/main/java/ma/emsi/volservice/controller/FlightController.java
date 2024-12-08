package ma.emsi.volservice.controller;

import ma.emsi.volservice.dto.flight.FlightRequest;
import ma.emsi.volservice.dto.flight.FlightResponse;
import ma.emsi.volservice.service.FlightIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
public class FlightController {
    private final FlightIService flightService;

    public FlightController(FlightIService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<FlightResponse> getAllAirlines() {
        return flightService.getAllFlights();
    }
    @PostMapping
    ResponseEntity<String> createAirline(@RequestBody FlightRequest flightRequest) {
        System.out.println("Received AirlineRequest: " + flightRequest);
        flightService.addFlight(flightRequest);
        return new ResponseEntity<>(String.format("Le vol NO~%s a été créé avec succès.",flightRequest.getFlightNo()), HttpStatus.CREATED);
    }
}
