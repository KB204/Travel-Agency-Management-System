package net.travelsystem.hotelservice.service.specification;

import net.travelsystem.hotelservice.entities.Convention;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.Optional;

public class ConventionSpecification {

    public static Specification<Convention> filterWithoutConditions(){
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Convention> identifierEqual(String identifier){
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(identifier)
                        .map(convention -> criteriaBuilder.equal(root.get("identifier"),identifier))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Convention> availableRoomsEqual(Integer nbr){
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(nbr)
                        .map(convention -> criteriaBuilder.equal(root.get("availableRooms"),nbr))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Convention> checkInDateLike(String checkIn){
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(checkIn)
                        .map(convention -> criteriaBuilder.like(root.get("checkInDate").as(String.class),
                                checkIn + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Convention> checkOutDateLike(String checkOut){
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(checkOut)
                        .map(convention -> criteriaBuilder.like(root.get("checkOutDate").as(String.class),
                                checkOut + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Convention> conventionDateBetween(LocalDate start,LocalDate end){
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (start != null){
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("checkInDate"),start));
            }

            if (end != null){
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("checkOutDate"),end));
            }
            return predicate;
        };
    }
}
