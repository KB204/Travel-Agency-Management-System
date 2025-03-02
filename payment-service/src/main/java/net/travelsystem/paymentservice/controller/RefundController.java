package net.travelsystem.paymentservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.paymentservice.dto.refund.RefundRequest;
import net.travelsystem.paymentservice.dto.refund.RefundResponse;
import net.travelsystem.paymentservice.service.RefundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static java.lang.String.format;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {
    private final RefundService service;

    public RefundController(RefundService service) {
        this.service = service;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    Page<RefundResponse> findAllRefunds(
            @RequestParam(required = false) String identifier,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return service.findAllRefunds(identifier, amount, date, status, pageable);
    }

    @PostMapping("/{identifier}/newRefund")
    ResponseEntity<String> createNewRefund(@PathVariable String identifier) {
        service.saveNewRefund(identifier);
        return new ResponseEntity<>("La demande de remboursement a été crée avec succès",HttpStatus.CREATED);
    }

    @PutMapping("/{identifier}/decline")
    ResponseEntity<String> declineRefund(@PathVariable String identifier) {
        service.declineRefund(identifier);
        return new ResponseEntity<>(format("Le remboursement identifié par %s a été rejeté",identifier),HttpStatus.ACCEPTED);
    }

    @PutMapping("/{identifier}/accept")
    ResponseEntity<String> acceptRefund(@PathVariable String identifier, @RequestBody @Valid RefundRequest request) {
        service.acceptRefund(identifier, request);
        return new ResponseEntity<>(format("Le remboursement identifié par %s a été accepté",identifier),HttpStatus.ACCEPTED);
    }
}
