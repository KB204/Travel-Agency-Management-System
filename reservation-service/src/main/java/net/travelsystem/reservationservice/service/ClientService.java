package net.travelsystem.reservationservice.service;

import net.travelsystem.reservationservice.dto.client.ClientResponse;

import java.util.List;

public interface ClientService {
    List<ClientResponse> getAllCustomers();
    long calculateClientReservations(String identity);
}
