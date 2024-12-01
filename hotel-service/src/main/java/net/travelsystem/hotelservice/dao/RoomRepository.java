package net.travelsystem.hotelservice.dao;

import net.travelsystem.hotelservice.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    Optional<Room> findByRoomNumberIgnoreCaseAndHotel_NameAndHotel_Location(String roomNumber,String name,String location);
    List<Room> findByHotelNameAndHotelLocation(String name, String location);
}
