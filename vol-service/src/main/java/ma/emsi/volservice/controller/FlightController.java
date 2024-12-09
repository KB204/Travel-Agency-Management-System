package ma.emsi.volservice.controller;

import jakarta.validation.Valid;
import ma.emsi.volservice.dto.flight.FlightRequest;
import ma.emsi.volservice.dto.flight.FlightResponse;
import ma.emsi.volservice.service.FlightIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
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
    ResponseEntity<String> createAirline(@RequestBody @Valid FlightRequest flightRequest) {
        flightService.addFlight(flightRequest);
        return new ResponseEntity<>("Vol a été créé avec succès",HttpStatus.CREATED);
    }
}
