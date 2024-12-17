package net.travelsystem.hotelservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.hotelservice.dto.convention.ConventionRequest;
import net.travelsystem.hotelservice.dto.convention.ConventionResponse;
import net.travelsystem.hotelservice.service.ConventionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/conventions")
@CrossOrigin(origins = "http://localhost:4200")
public class ConventionController {
    private final ConventionService service;

    public ConventionController(ConventionService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ConventionResponse> findAllHotelsConventions(
            @RequestParam(required = false) String identifier,
            @RequestParam(required = false) Integer nbr,
            @RequestParam(required = false) String checkIn,
            @RequestParam(required = false) String checkOut,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end,
            Pageable pageable) {
        return service.getAllConventions(identifier, nbr, checkIn, checkOut, start, end, pageable);
    }

    @GetMapping("/{identifier}/details")
    ResponseEntity<ConventionResponse> findConventionByIdentifier(@PathVariable String identifier) {
        ConventionResponse conventionDetails = service.getConventionDetails(identifier);
        return new ResponseEntity<>(conventionDetails,HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> saveNewHotelConvention(@RequestBody @Valid ConventionRequest request){
        service.createHotelConvention(request);
        return new ResponseEntity<>(String.format("La convention avec l'hotel %s situé a %s a été créé avec succès",request.name(),request.location()),HttpStatus.CREATED);
    }
}
