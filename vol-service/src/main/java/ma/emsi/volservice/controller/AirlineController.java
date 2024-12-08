package ma.emsi.volservice.controller;

import jakarta.validation.Valid;
import ma.emsi.volservice.dto.airline.AirlineRequest;
import ma.emsi.volservice.dto.airline.AirlineResponse;
import ma.emsi.volservice.service.AirlineIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airline")
public class AirlineController {
    private final AirlineIService airlineService;

    public AirlineController(AirlineIService airlineService) {
        this.airlineService = airlineService;
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<AirlineResponse> getAllAirlines() {
       return airlineService.getAllAirline();
    }
    @PostMapping
    ResponseEntity<String> createAirline(@RequestBody AirlineRequest airlineRequest) {
        System.out.println("Received AirlineRequest: " + airlineRequest);
        airlineService.createAirline(airlineRequest);
        return new ResponseEntity<>(String.format("Airline %s a été créé avec succès",airlineRequest.getName()), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    ResponseEntity<String> updateAirline(@PathVariable Long id, @RequestBody @Valid AirlineRequest airlineRequest) {
        airlineService.updateAirline(id, airlineRequest);
        return new ResponseEntity<>(String.format("Airline %s a été modifié avec succès",airlineRequest.getName()), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirline(id);
        return ResponseEntity.noContent().build();
    }
}
