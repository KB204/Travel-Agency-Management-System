package net.travelsystem.reservationservice.service.specification;

import net.travelsystem.reservationservice.entities.Reservation;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class ReservationSpecification {

    public static Specification<Reservation> filterWithoutConditions() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Reservation> identifierEqual(String identifier) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(identifier)
                        .map(reservation -> criteriaBuilder.equal(root.get("identifier"),identifier))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Reservation> statusEqual(String status) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(status)
                        .map(reservation -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")),
                                "%" + status + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Reservation> amountEqual(Double amount) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(amount)
                        .map(reservation -> criteriaBuilder.equal(root.get("totalPrice"),amount))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Reservation> reservationDateLike(String date) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(date)
                        .map(reservation -> criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class,
                                root.get("reservationDate"), criteriaBuilder.literal("YYYY-MM-DD")), date + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }
}
