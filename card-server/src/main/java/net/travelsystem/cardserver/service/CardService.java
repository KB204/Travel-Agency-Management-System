package net.travelsystem.cardserver.service;

import net.travelsystem.cardserver.dto.CardRequest;
import net.travelsystem.cardserver.dto.CardResponse;
import net.travelsystem.cardserver.dto.UpdateRequest;
import net.travelsystem.paymentservice.dto.event.PaymentEvent;

import java.util.List;

public interface CardService {
    List<CardResponse> getAllCards();
    void createCard(CardRequest request);
    void updateCard(UpdateRequest request);
    void debitCard(PaymentEvent payment);
    CardResponse findCardByNumber(String cardNumber);
}
