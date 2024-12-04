package net.travelsystem.hotelservice.service.specification;

import net.travelsystem.hotelservice.entities.Hotel;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class HotelSpecification {

    public static Specification<Hotel> filterWithoutConditions(){
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Hotel> hotelNameLike(String name){
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(name)
                        .map(hotel -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Hotel> hotelLocationLike(String location){
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(location)
                        .map(hotel -> criteriaBuilder.like(criteriaBuilder.lower(root.get("location")),
                                "%" + location.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }
}
