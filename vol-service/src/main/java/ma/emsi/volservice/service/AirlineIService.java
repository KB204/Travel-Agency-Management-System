package ma.emsi.volservice.service;

import ma.emsi.volservice.dto.airline.AirlineRequest;
import ma.emsi.volservice.dto.airline.AirlineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface AirlineIService {
    Page<AirlineResponse> getAllAirline(String name, String code, Integer max, Pageable pageable);
    void createAirline(AirlineRequest airlineRequest);
    void updateAirline(Long id, AirlineRequest airlineRequest);
    void deleteAirline(Long id);
}
