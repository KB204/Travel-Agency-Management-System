package net.travelsystem.reservationservice.controller;

import net.travelsystem.reservationservice.dto.client.ClientResponse;
import net.travelsystem.reservationservice.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {
    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ClientResponse> findAllClients() {
        return service.getAllCustomers();
    }
}
