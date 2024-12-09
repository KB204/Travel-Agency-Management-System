package ma.emsi.volservice.dto.flight;


import ma.emsi.volservice.dto.airline.AirlineResponseDTO;
import ma.emsi.volservice.enums.FlightType;

import java.time.LocalDateTime;

public record FlightResponse(
        String flightNo,
        String origin,
        String destination,
        FlightType flightType,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        AirlineResponseDTO airline){}
