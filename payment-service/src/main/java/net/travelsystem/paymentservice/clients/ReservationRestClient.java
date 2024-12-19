package net.travelsystem.paymentservice.clients;

import net.travelsystem.paymentservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8082",name = "reservation-service",configuration = FeignConfig.class)
public interface ReservationRestClient {

    @GetMapping("/api/reservations/{identifier}/reservation/totalPrice")
    Double getReservationTotalAmount(@PathVariable String identifier);
}
