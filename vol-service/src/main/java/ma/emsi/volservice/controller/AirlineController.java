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
@RequestMapping("/api/airlines")
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
    ResponseEntity<String> createAirline(@RequestBody @Valid AirlineRequest airlineRequest) {
        airlineService.createAirline(airlineRequest);
        return new ResponseEntity<>(String.format("Compagnie aérienne %s identifié par %s a été créé avec succès",airlineRequest.name(),airlineRequest.code()), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    ResponseEntity<String> updateAirline(@PathVariable Long id, @RequestBody @Valid AirlineRequest airlineRequest) {
        airlineService.updateAirline(id, airlineRequest);
        return new ResponseEntity<>(String.format("Compagnie aérienne %s a été modifié avec succès",airlineRequest.name()), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirline(id);
        return ResponseEntity.noContent().build();
    }
}
