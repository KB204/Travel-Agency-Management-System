package ma.emsi.volservice.service.specification;

import ma.emsi.volservice.model.Flight;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class FlightSpecification {

     public static Specification<Flight> filterWithoutConditions() {
         return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
     }

    public static Specification<Flight> flightNoEqual(String flightNo) {
         return (root, query, criteriaBuilder) ->
                 Optional.ofNullable(flightNo)
                         .map(flight -> criteriaBuilder.equal(root.get("flightNo"),flightNo))
                         .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Flight> flightTypeEqual(String type) {
         return (root, query, criteriaBuilder) ->
                 Optional.ofNullable(type)
                         .map(flight -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("flightType")),
                                 type.toLowerCase()))
                         .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Flight> flightOriginLike(String origin) {
         return (root, query, criteriaBuilder) ->
                 Optional.ofNullable(origin)
                         .map(flight -> criteriaBuilder.like(criteriaBuilder.lower(root.get("origin")),
                                 "%" + origin.toLowerCase() + "%"))
                         .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Flight> flightDestinationLike(String destination) {
         return (root, query, criteriaBuilder) ->
                 Optional.ofNullable(destination)
                         .map(flight -> criteriaBuilder.like(criteriaBuilder.lower(root.get("destination")),
                                 "%" + destination.toLowerCase() + "%"))
                         .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Flight> flightAirlineLike(String airline) {
         return (root, query, criteriaBuilder) ->
                 Optional.ofNullable(airline)
                         .map(flight -> criteriaBuilder.like(criteriaBuilder.lower(root.get("airline").get("name")),
                                 "%" + airline.toLowerCase() + "%"))
                         .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Flight> flightDepartureTimeLike(String depDate) {
         return (root, query, criteriaBuilder) ->
                 Optional.ofNullable(depDate)
                         .map(reservation -> criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class,
                                 root.get("departureTime"), criteriaBuilder.literal("YYYY-MM-DD")), depDate + "%"))
                         .orElse(criteriaBuilder.conjunction());

    }
}
