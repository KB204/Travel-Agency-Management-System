package net.travelsystem.cardserver.controller;

import jakarta.validation.Valid;
import net.travelsystem.cardserver.dto.CardRequest;
import net.travelsystem.cardserver.dto.CardResponse;
import net.travelsystem.cardserver.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardService service;

    public CardController(CardService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<CardResponse> findCards() {
         return service.getAllCards();
    }

    @GetMapping("/{cardNumber}/cardDetails")
    ResponseEntity<CardResponse> findCard(@PathVariable String cardNumber) {
        CardResponse card = service.findCardByNumber(cardNumber);
        return new ResponseEntity<>(card,HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> saveNewCard(@RequestBody @Valid CardRequest request) {
        service.createCard(request);
        return new ResponseEntity<>(String.format("Cart bancaire numéro %s a été crée avec succès",request.cardNumber()),HttpStatus.CREATED);
    }
}
