package ma.emsi.volservice.service.specification;

import ma.emsi.volservice.model.Airline;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class AirlineSpecification {

    public static Specification<Airline> filterWithoutConditions() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Airline> airlineNameLike(String name) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(name)
                        .map(airline -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                        "%" + name.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Airline> airlineCodeEqual(String code) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(code)
                        .map(airline -> criteriaBuilder.equal(root.get("code"),code))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Airline> airlineCapacityEqual(Integer max) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(max)
                        .map(airline -> criteriaBuilder.equal(root.get("fleetSize"),max))
                        .orElse(criteriaBuilder.conjunction());
    }
}
