package net.travelsystem.reservationservice.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import net.travelsystem.reservationservice.config.FeignConfig;
import net.travelsystem.reservationservice.dto.external_services.HotelConvention;
import net.travelsystem.reservationservice.dto.external_services.HotelConventionDTO;
import net.travelsystem.reservationservice.dto.external_services.HotelResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@FeignClient(value = "HOTEL-SERVICE",configuration = FeignConfig.class)
public interface HotelConventionRest {

    @GetMapping("/api/conventions/{identifier}/details")
    HotelConvention getHotelConventionDetails(@PathVariable String identifier);

    @CircuitBreaker(name = "externalService",fallbackMethod = "defaultHotelConvention")
    @Retry(name = "retryExternalService")
    @GetMapping("/api/conventions/{identifier}/details")
    HotelConvention findHotelConvention(@PathVariable String identifier);
    @CircuitBreaker(name = "externalService",fallbackMethod = "defaultResponse")
    @Retry(name = "retryExternalService")
    @GetMapping("/api/conventions/{identifier}/hotelConventionDetails")
    HotelConventionDTO findHotel(@PathVariable String identifier);

    default HotelConvention defaultHotelConvention(String identifier, Exception ex){
        return HotelConvention.builder()
                .identifier(identifier)
                .availableRooms(0)
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now())
                .hotel(new HotelResponseDTO("hotel non trouvé","hotel non trouvé"))
                .build();
    }

    default HotelConventionDTO defaultResponse(String identifier,Exception ex){
        return new HotelConventionDTO(new HotelResponseDTO("hotel non trouvé","hotel non trouvé"));
    }
}
