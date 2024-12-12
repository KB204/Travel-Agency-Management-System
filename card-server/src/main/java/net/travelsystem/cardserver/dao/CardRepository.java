package net.travelsystem.cardserver.dao;

import net.travelsystem.cardserver.entity.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CardRepository extends MongoRepository<Card,String> {
    Optional<Card> findByCardNumber(String cardNumber);
}
