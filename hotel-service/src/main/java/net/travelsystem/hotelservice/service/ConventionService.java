package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dto.convention.ConventionRequest;
import net.travelsystem.hotelservice.dto.convention.ConventionResponse;

import java.util.List;

public interface ConventionService {
    List<ConventionResponse> getAllConventions();
    void createHotelConvention(ConventionRequest request);
}
