package ma.emsi.volservice.service;

import ma.emsi.volservice.dto.airline.AirlineRequest;
import ma.emsi.volservice.dto.airline.AirlineResponse;

import java.util.List;


public interface AirlineIService {
    List<AirlineResponse> getAllAirline();
    void createAirline(AirlineRequest airlineRequest);
    void updateAirline(Long id, AirlineRequest airlineRequest);
    void deleteAirline(Long airlineId);
}
