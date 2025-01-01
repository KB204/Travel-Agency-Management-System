package net.travelsystem.reservationservice.service;

import net.travelsystem.reservationservice.dao.ClientRepository;
import net.travelsystem.reservationservice.dto.client.ClientResponse;
import net.travelsystem.reservationservice.mapper.ClientMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


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
}
