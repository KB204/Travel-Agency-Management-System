package net.travelsystem.hotelservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.hotelservice.dto.convention.ConventionRequest;
import net.travelsystem.hotelservice.dto.convention.ConventionResponse;
import net.travelsystem.hotelservice.service.ConventionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conventions")
public class ConventionController {
    private final ConventionService service;

    public ConventionController(ConventionService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ConventionResponse> findAllHotelsConventions() {
        return service.getAllConventions();
    }

    @PostMapping
    ResponseEntity<String> saveNewHotelConvention(@RequestBody @Valid ConventionRequest request){
        service.createHotelConvention(request);
        return new ResponseEntity<>(String.format("La convention avec l'hotel %s situé a %s a été créé avec succès",request.name(),request.location()),HttpStatus.CREATED);
    }
}
