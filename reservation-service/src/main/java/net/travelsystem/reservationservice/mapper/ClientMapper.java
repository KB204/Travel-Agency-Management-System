package net.travelsystem.reservationservice.mapper;

import net.travelsystem.reservationservice.dto.client.ClientResponse;
import net.travelsystem.reservationservice.entities.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientResponse clientToDtoResponse(Client client);
}
