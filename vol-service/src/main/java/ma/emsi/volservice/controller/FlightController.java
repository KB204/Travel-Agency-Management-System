package ma.emsi.volservice.controller;

import jakarta.validation.Valid;
import ma.emsi.volservice.dto.flight.FlightRequest;
import ma.emsi.volservice.dto.flight.FlightResponse;
import ma.emsi.volservice.service.FlightIService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:4200")
public class FlightController {
    private final FlightIService flightService;

    public FlightController(FlightIService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<FlightResponse> getAllAirlines(
            @RequestParam(required = false) String flightNo,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) String depDate,
            Pageable pageable) {
        return flightService.getAllFlights(flightNo, type, origin, destination, airline, depDate, pageable);
    }

    @PostMapping
    ResponseEntity<String> createAirline(@RequestBody @Valid FlightRequest flightRequest) {
        flightService.addFlight(flightRequest);
        return new ResponseEntity<>("Vol a été créé avec succès",HttpStatus.CREATED);
    }
}
