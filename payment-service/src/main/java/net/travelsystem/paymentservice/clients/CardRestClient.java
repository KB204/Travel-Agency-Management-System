package net.travelsystem.paymentservice.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import net.travelsystem.paymentservice.dto.external_services.CardResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDate;


public interface CardRestClient {
    @GetExchange("/{cardNumber}/cardDetails")
    CardResponse findCardByNumber(@PathVariable String cardNumber);

    @CircuitBreaker(name = "externalApi",fallbackMethod = "getDefaultMessage")
    @Retry(name = "retryExternalApi")
    @GetExchange("/{cardNumber}/cardDetails")
    CardResponse getCard(@PathVariable String cardNumber);

    default CardResponse getDefaultMessage(String cardNumber,Exception ex) {
        return CardResponse.builder()
                .cardNumber(cardNumber)
                .cardOwner("Carte non trouvée")
                .currency("Carte non trouvée")
                .balance(0.0)
                .verificationCode(0)
                .expirationDate(LocalDate.now())
                .build();
    }
}
