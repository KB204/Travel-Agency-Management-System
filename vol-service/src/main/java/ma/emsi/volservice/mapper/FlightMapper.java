package ma.emsi.volservice.mapper;

import ma.emsi.volservice.dto.flight.FlightRequest;
import ma.emsi.volservice.dto.flight.FlightResponse;
import ma.emsi.volservice.model.Flight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    FlightResponse flightToDtoResponse(Flight flight);
    Flight dtoRequestToFlight(FlightRequest request);
}
