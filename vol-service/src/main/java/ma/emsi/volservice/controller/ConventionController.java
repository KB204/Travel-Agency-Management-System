package ma.emsi.volservice.controller;

import jakarta.validation.Valid;
import ma.emsi.volservice.dto.convention.ConventionRequest;
import ma.emsi.volservice.dto.convention.ConventionResponse;
import ma.emsi.volservice.dto.convention.ConventionResponseDTO;
import ma.emsi.volservice.service.ConventionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/flightsConventions")
@CrossOrigin(origins = "http://localhost:4200")
public class ConventionController {
    private final ConventionService service;

    public ConventionController(ConventionService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ConventionResponse> getAllFlightsConventions(
            @RequestParam(required = false) Integer nbr,
            @RequestParam(required = false) String flightNo,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String depTime,
            @RequestParam(required = false) String arrivalTime,
            Pageable pageable) {
        return service.getAllConventions(nbr, flightNo, origin, destination, depTime, arrivalTime, pageable);
    }

    @GetMapping("/{flightNo}/conventionDetails")
    ResponseEntity<ConventionResponse> getFlightConventionDetails(@PathVariable String flightNo) {
        ConventionResponse conventionDetails = service.getConventionDetails(flightNo);
        return new ResponseEntity<>(conventionDetails,HttpStatus.OK);
    }

    @GetMapping("/{flightNo}/flightConventionDetails")
    ResponseEntity<ConventionResponseDTO> getFlightDetails(@PathVariable String flightNo) {
        ConventionResponseDTO conventionFlightDetails = service.getConventionFlightDetails(flightNo);
        return new ResponseEntity<>(conventionFlightDetails,HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> saveNewFlightConvention(@RequestBody @Valid ConventionRequest request) {
        service.createNewConvention(request);
        return new ResponseEntity<>(String.format("La convention identifié par %s a été créé avec succès",request.flightNo()),HttpStatus.CREATED);
    }
}
