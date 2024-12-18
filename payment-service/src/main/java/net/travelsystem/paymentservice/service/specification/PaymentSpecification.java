package net.travelsystem.paymentservice.service.specification;

import net.travelsystem.paymentservice.entities.Payment;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class PaymentSpecification {

    public static Specification<Payment> filterWithoutConditions() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Payment> paymentIdentifierEqual(String identifier) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(identifier)
                        .map(payment -> criteriaBuilder.equal(root.get("transactionIdentifier"),identifier))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Payment> paymentDateLike(String date) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(date)
                        .map(payment -> criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class,
                                root.get("createdAt"), criteriaBuilder.literal("YYYY-MM-DD")),
                                date + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Payment> paymentAmountEqual(Double amount) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(amount)
                        .map(payment -> criteriaBuilder.equal(root.get("amount"),amount))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Payment> cardNumberEqual(String cardNumber) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(cardNumber)
                        .map(payment -> criteriaBuilder.equal(root.get("cardNumber"),cardNumber))
                        .orElse(criteriaBuilder.conjunction());
    }
}
