package ma.emsi.volservice.controller;

import jakarta.validation.Valid;
import ma.emsi.volservice.dto.airline.AirlineRequest;
import ma.emsi.volservice.dto.airline.AirlineResponse;
import ma.emsi.volservice.service.AirlineIService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/airlines")
public class AirlineController {
    private final AirlineIService airlineService;

    public AirlineController(AirlineIService airlineService) {
        this.airlineService = airlineService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<AirlineResponse> getAllAirlines(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Integer max,
            Pageable pageable) {
       return airlineService.getAllAirline(name, code, max, pageable);
    }

    @PostMapping
    ResponseEntity<String> createAirline(@RequestBody @Valid AirlineRequest airlineRequest) {
        airlineService.createAirline(airlineRequest);
        return new ResponseEntity<>(String.format("Compagnie aérienne %s identifié par %s a été créé avec succès",airlineRequest.name(),airlineRequest.code()), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<String> updateAirline(@PathVariable Long id, @RequestBody @Valid AirlineRequest airlineRequest) {
        airlineService.updateAirline(id, airlineRequest);
        return new ResponseEntity<>(String.format("Compagnie aérienne %s a été modifié avec succès",airlineRequest.name()), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<String> deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirline(id);
        return ResponseEntity.noContent().build();
    }
}
