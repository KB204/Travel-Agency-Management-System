package net.travelsystem.paymentservice.service.specification;

import net.travelsystem.paymentservice.entities.Refund;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class RefundSpecification {

    public static Specification<Refund> filterWithoutConditions() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Refund> refundIdentifierEqual(String identifier) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(identifier)
                        .map(refund -> criteriaBuilder.equal(root.get("payment").get("transactionIdentifier"),identifier))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Refund> refundAmountEqual(Double amount) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(amount)
                        .map(refund -> criteriaBuilder.equal(root.get("amount"),amount))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Refund> refundStatusEqual(String status) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(status)
                        .map(refund -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")),
                                status.toLowerCase()))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Refund> refundDateLike(String date) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(date)
                        .map(refund -> criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class,
                                root.get("processedAt"), criteriaBuilder.literal("YYYY-MM-DD")), date + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }
}
