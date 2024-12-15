package net.travelsystem.cardserver.service;

import net.travelsystem.cardserver.dto.CardRequest;
import net.travelsystem.cardserver.dto.CardResponse;
import net.travelsystem.cardserver.dto.UpdateRequest;
import net.travelsystem.paymentservice.dto.event.PaymentEvent;
import net.travelsystem.cardserver.dao.CardRepository;
import net.travelsystem.cardserver.entity.Card;
import net.travelsystem.cardserver.exceptions.ResourceAlreadyExists;
import net.travelsystem.cardserver.exceptions.ResourceNotFoundException;
import net.travelsystem.cardserver.mapper.CardMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper mapper;

    public CardServiceImpl(CardRepository cardRepository, CardMapper mapper) {
        this.cardRepository = cardRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CardResponse> getAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(mapper::cardToDtoResponse)
                .toList();
    }

    @Override
    public void createCard(CardRequest request) {
        cardRepository.findByCardNumber(request.cardNumber())
                .ifPresent(card -> {
                    throw new ResourceAlreadyExists(String.format("La carte numéro %s exists déja",request.cardNumber()));
                });

        Card card = mapper.requestDtoToCard(request);
        cardRepository.save(card);
    }

    @Override
    public void updateCard(UpdateRequest request) {
        Card card = cardRepository.findByCardNumber(request.cardNumber())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Cart numéro %s n'existe pas",request.cardNumber())));

        card.setBalance(card.getBalance() + request.amount());
        cardRepository.save(card);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.payment.name}",groupId = "${spring.kafka.consumer.group-id}")
    public void debitCard(PaymentEvent payment) {
        Card card = cardRepository.findByCardNumber(payment.cardNumber())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Cart numéro %s n'existe pas",payment.cardNumber())));

        card.setBalance(card.getBalance() - payment.amount());
        cardRepository.save(card);
    }

    @Override
    public CardResponse findCardByNumber(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Cart numéro %s n'existe pas",cardNumber)));

        return mapper.cardToDtoResponse(card);
    }
}
