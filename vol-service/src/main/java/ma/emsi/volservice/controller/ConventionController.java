package ma.emsi.volservice.controller;

import jakarta.validation.Valid;
import ma.emsi.volservice.dto.convention.ConventionRequest;
import ma.emsi.volservice.dto.convention.ConventionResponse;
import ma.emsi.volservice.service.ConventionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flightsConventions")
public class ConventionController {
    private final ConventionService service;

    public ConventionController(ConventionService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ConventionResponse> getAllFlightsConventions() {
        return service.getAllConventions();
    }

    @GetMapping("/{flightNo}/conventionDetails")
    ResponseEntity<ConventionResponse> getFlightConventionDetails(@PathVariable String flightNo) {
        ConventionResponse conventionDetails = service.getConventionDetails(flightNo);
        return new ResponseEntity<>(conventionDetails,HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> saveNewFlightConvention(@RequestBody @Valid ConventionRequest request) {
        service.createNewConvention(request);
        return new ResponseEntity<>(String.format("La convention identifié par %s a été créé avec succès",request.flightNo()),HttpStatus.CREATED);
    }
}
