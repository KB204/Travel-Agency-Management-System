package ma.emsi.volservice.service.specification;

import ma.emsi.volservice.model.Convention;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class ConventionSpecification {

    public static Specification<Convention> filterWithoutConditions() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Convention> availablePlacesEqual(Integer nbr) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(nbr)
                        .map(convention -> criteriaBuilder.equal(root.get("availablePlaces"),nbr))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Convention> flightNoEqual(String flightNo) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(flightNo)
                        .map(convention -> criteriaBuilder.equal(root.get("flight").get("flightNo"),flightNo))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Convention> flightOriginLike(String origin) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(origin)
                        .map(convention -> criteriaBuilder.like(criteriaBuilder.lower(root.get("flight").get("origin")),
                                "%" + origin.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Convention> flightDestinationLike(String destination) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(destination)
                        .map(convention -> criteriaBuilder.like(criteriaBuilder.lower(root.get("flight").get("destination")),
                                "%" + destination.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Convention> flightDepartureTimeLike(String depTime) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(depTime)
                        .map(convention -> criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class,
                                root.get("flight").get("departureTime"), criteriaBuilder.literal("YYYY-MM-DD")),
                                depTime + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Convention> flightArrivalTimeLike(String arrivalTime) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(arrivalTime)
                        .map(convention -> criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class,
                                        root.get("flight").get("arrivalTime"), criteriaBuilder.literal("YYYY-MM-DD")),
                                arrivalTime + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

}
