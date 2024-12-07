package net.travelsystem.reservationservice.service.specification;

import net.travelsystem.reservationservice.entities.Trip;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class TripSpecification {

    public static Specification<Trip> filterWithoutConditions() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Trip> nameLike(String name) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(name)
                        .map(trip -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Trip> priceEqual(Double price) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(price)
                        .map(trip -> criteriaBuilder.equal(root.get("price"),price))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Trip> destinationLike(String destination) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(destination)
                        .map(trip -> criteriaBuilder.like(criteriaBuilder.lower(root.get("destination")),
                                "%" + destination.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Trip> hotelConventionEqual(String identifier) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(identifier)
                        .map(trip -> criteriaBuilder.equal(root.get("hotelConventionIdentifier"),identifier))
                        .orElse(criteriaBuilder.conjunction());
    }
}
