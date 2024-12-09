package ma.emsi.volservice.mapper;

import ma.emsi.volservice.dto.airline.AirlineRequest;
import ma.emsi.volservice.dto.airline.AirlineResponse;
import ma.emsi.volservice.model.Airline;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AirlineMapper {

    AirlineResponse airlineToDtoResponse(Airline airline);
    Airline requestDtoToAirline(AirlineRequest request);
}
