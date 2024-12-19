package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dto.convention.ConventionRequest;
import net.travelsystem.hotelservice.dto.convention.ConventionResponse;
import net.travelsystem.hotelservice.dto.convention.ConventionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ConventionService {
    Page<ConventionResponse> getAllConventions(String identifier, Integer nbr, String checkIn, String checkOut,
                                               LocalDate start, LocalDate end, Pageable pageable);
    void createHotelConvention(ConventionRequest request);
    ConventionResponse getConventionDetails(String identifier);
    ConventionResponseDTO getConventionHotelDetails(String identifier);
}
