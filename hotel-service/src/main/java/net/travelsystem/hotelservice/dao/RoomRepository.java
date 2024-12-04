package net.travelsystem.hotelservice.dao;

import net.travelsystem.hotelservice.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long>, JpaSpecificationExecutor<Room> {
    Optional<Room> findByRoomNumberIgnoreCaseAndHotel_NameAndHotel_Location(String roomNumber,String name,String location);
}
