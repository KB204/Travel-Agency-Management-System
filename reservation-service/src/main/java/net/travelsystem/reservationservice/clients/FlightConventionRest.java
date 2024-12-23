package net.travelsystem.reservationservice.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import net.travelsystem.reservationservice.config.FeignConfig;
import net.travelsystem.reservationservice.dto.external_services.AirlineResponseDTO;
import net.travelsystem.reservationservice.dto.external_services.FlightConvention;
import net.travelsystem.reservationservice.dto.external_services.FlightConventionDTO;
import net.travelsystem.reservationservice.dto.external_services.FlightResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@FeignClient(value = "VOL-SERVICE",configuration = FeignConfig.class)
public interface FlightConventionRest {

    @GetMapping("/api/flightsConventions/{flightNo}/conventionDetails")
    FlightConvention getFlightConventionDetails(@PathVariable String flightNo);

    @CircuitBreaker(name = "externalService",fallbackMethod = "defaultFlightConvention")
    @Retry(name = "retryExternalService")
    @GetMapping("/api/flightsConventions/{flightNo}/conventionDetails")
    FlightConvention findFlightConvention(@PathVariable String flightNo);

    @CircuitBreaker(name = "externalService",fallbackMethod = "defaultMessage")
    @Retry(name = "retryExternalService")
    @GetMapping("/api/flightsConventions/{flightNo}/flightConventionDetails")
    FlightConventionDTO findFlight(@PathVariable String flightNo);

    default FlightConvention defaultFlightConvention(String flightNo,Exception ex){
        return new FlightConvention(0,FlightResponse.builder()
                .flightNo(flightNo)
                .flightType("Vol non trouvé")
                .origin("Vol non trouvé")
                .destination("Vol non trouvé")
                .airline(new AirlineResponseDTO("Compagnie aérienne non trouvée","Compagnie aérienne non trouvée"))
                .arrivalTime(LocalDateTime.now())
                .departureTime(LocalDateTime.now())
                .build());
    }

    default FlightConventionDTO defaultMessage(String flightNo,Exception ex){
        return new FlightConventionDTO(FlightResponse.builder()
                .flightNo(flightNo)
                .flightType("Vol non trouvé")
                .origin("Vol non trouvé")
                .destination("Vol non trouvé")
                .airline(new AirlineResponseDTO("Compagnie aérienne non trouvée","Compagnie aérienne non trouvée"))
                .arrivalTime(LocalDateTime.now())
                .departureTime(LocalDateTime.now())
                .build());
    }
}
