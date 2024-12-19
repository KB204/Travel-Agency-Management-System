package ma.emsi.volservice.service;

import ma.emsi.volservice.dto.convention.ConventionRequest;
import ma.emsi.volservice.dto.convention.ConventionResponse;
import ma.emsi.volservice.dto.convention.ConventionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ConventionService {
    Page<ConventionResponse> getAllConventions(Integer nbr, String flightNo, String origin, String destination, String depTime, String arrivalTime, Pageable pageable);
    void createNewConvention(ConventionRequest request);
    ConventionResponse getConventionDetails(String flightNo);
    ConventionResponseDTO getConventionFlightDetails(String flightNo);
}
