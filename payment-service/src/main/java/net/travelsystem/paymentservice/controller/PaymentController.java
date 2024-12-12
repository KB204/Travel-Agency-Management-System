package net.travelsystem.paymentservice.controller;

import jakarta.validation.Valid;
import net.travelsystem.paymentservice.dto.payment.PaymentRequest;
import net.travelsystem.paymentservice.dto.payment.PaymentResponse;
import net.travelsystem.paymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<PaymentResponse> findAllPayments() { return service.getAllPayments(); }

    @PostMapping("/newPayment")
    ResponseEntity<String> saveNewPayment(@RequestParam Long id, @RequestBody @Valid PaymentRequest request) {
        service.newPayment(id, request);
        return new ResponseEntity<>("Paiment a été effectué avec succès",HttpStatus.CREATED);
    }
}
