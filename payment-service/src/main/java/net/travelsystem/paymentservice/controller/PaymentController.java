package net.travelsystem.paymentservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.paymentservice.dto.payment.PaymentRequest;
import net.travelsystem.paymentservice.dto.payment.PaymentResponse;
import net.travelsystem.paymentservice.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<PaymentResponse> findAllPayments(
            @RequestParam(required = false) String identifier,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) String cardNumber,
            Pageable pageable) {
        return service.getAllPayments(identifier, date, amount, cardNumber, pageable);
    }

    @PostMapping("/newPayment")
    ResponseEntity<String> saveNewPayment(@RequestParam String reservationIdentifier, @RequestBody @Valid PaymentRequest request) {
        service.newPayment(reservationIdentifier, request);
        return new ResponseEntity<>("Paiment a été effectué avec succès",HttpStatus.CREATED);
    }
}
