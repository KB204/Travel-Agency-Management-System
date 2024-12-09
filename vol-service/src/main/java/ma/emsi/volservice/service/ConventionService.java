package ma.emsi.volservice.service;

import ma.emsi.volservice.dto.convention.ConventionRequest;
import ma.emsi.volservice.dto.convention.ConventionResponse;

import java.util.List;

public interface ConventionService {
    List<ConventionResponse> getAllConventions();
    void createNewConvention(ConventionRequest request);
    ConventionResponse getConventionDetails(String flightNo);
}
