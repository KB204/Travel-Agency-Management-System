package net.travelsystem.hotelservice.service.specification;

import net.travelsystem.hotelservice.entities.Room;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class RoomSpecification {

    public static Specification<Room> filterWithoutConditions(){
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Room> onlySpecificHotel(String name,String location){
        return (root, query, criteriaBuilder) ->
                name == null || name.trim().isEmpty() || location == null || location.trim().isEmpty() ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.and(
                                criteriaBuilder.equal(criteriaBuilder.lower(root.get("hotel").get("name")),name.toLowerCase()),
                                criteriaBuilder.equal(criteriaBuilder.lower(root.get("hotel").get("location")),location.toLowerCase())
                        );

    }

    public static Specification<Room> roomNumberEqual(String roomNumber){
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(roomNumber)
                        .map(room -> criteriaBuilder.equal(root.get("roomNumber"),roomNumber))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Room> roomTypeEqual(String type){
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(type)
                        .map(room -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("roomType")),
                                type.toLowerCase()))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Room> roomPriceEqual(Double price){
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(price)
                        .map(room -> criteriaBuilder.equal(root.get("price"),price))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Room> onlyAvailableRooms(){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("available"));
    }
}
