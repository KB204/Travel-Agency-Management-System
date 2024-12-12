package net.travelsystem.cardserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "cards")
public class Card {
    @Id
    private String id;
    private String cardNumber;
    private String cardOwner;
    private Double balance;
    private LocalDate expirationDate;
    private Integer verificationCode;
    private String currency;
}
