package net.travelsystem.cardserver.service;

import net.travelsystem.cardserver.dto.CardRequest;
import net.travelsystem.cardserver.dto.CardResponse;

import java.util.List;

public interface CardService {
    List<CardResponse> getAllCards();
    void createCard(CardRequest request);
    CardResponse findCardByNumber(String cardNumber);
}
