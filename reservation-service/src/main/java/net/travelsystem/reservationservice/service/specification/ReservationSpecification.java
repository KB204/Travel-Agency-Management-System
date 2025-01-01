package net.travelsystem.reservationservice.service.specification;

import net.travelsystem.reservationservice.entities.Reservation;
import net.travelsystem.reservationservice.enums.ReservationStatus;
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
                                status.toLowerCase()))
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

    public static Specification<Reservation> clientIdentityEqual(String identity) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(identity)
                        .map(client -> criteriaBuilder.equal(root.get("client").get("identity"),identity))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Reservation> clientFirstNameLike(String firstName) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(firstName)
                        .map(client -> criteriaBuilder.like(criteriaBuilder.lower(root.get("client").get("firstName")),
                                "%" + firstName.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Reservation> clientLastNameLike(String lastName) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(lastName)
                        .map(client -> criteriaBuilder.like(criteriaBuilder.lower(root.get("client").get("lastName")),
                                "%" + lastName.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static  Specification<Reservation> clientApprovedReservations(String identity, ReservationStatus status) {
        return (root, query, criteriaBuilder) ->
                identity == null || identity.trim().isEmpty() || status == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get("client").get("identity"),identity),
                                criteriaBuilder.equal(root.get("status"),status)
                        );
    }
}
