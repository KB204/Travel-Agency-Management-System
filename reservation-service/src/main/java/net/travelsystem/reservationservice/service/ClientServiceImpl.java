package net.travelsystem.reservationservice.service;

import net.travelsystem.reservationservice.dao.ClientRepository;
import net.travelsystem.reservationservice.dto.client.ClientResponse;
import net.travelsystem.reservationservice.entities.Client;
import net.travelsystem.reservationservice.enums.ReservationStatus;
import net.travelsystem.reservationservice.exceptions.ResourceNotFoundException;
import net.travelsystem.reservationservice.mapper.ClientMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper mapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper mapper) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ClientResponse> getAllCustomers() {
        return clientRepository.findAll()
                .stream()
                .distinct()
                .sorted(Comparator.comparing(client -> client.getFirstName().toLowerCase()))
                .map(mapper::clientToDtoResponse)
                .toList();
    }

    @Override
    @Transactional
    public long calculateClientReservations(String identity) {
        Client client = clientRepository.findByIdentity(identity)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(format("Client identifié par %s n'existe pas",identity)));

        return client.getReservations()
                .stream()
                .filter(reservation -> reservation.getStatus().equals(ReservationStatus.APPROVED))
                .count();
    }
}
