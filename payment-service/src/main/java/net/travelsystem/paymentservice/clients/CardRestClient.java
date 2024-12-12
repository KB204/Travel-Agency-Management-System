package net.travelsystem.paymentservice.clients;

import net.travelsystem.paymentservice.dto.card.CardResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;


public interface CardRestClient {
    @GetExchange("/{cardNumber}/cardDetails")
    CardResponse findCardByNumber(@PathVariable String cardNumber);
}
