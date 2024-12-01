package net.travelsystem.hotelservice.dao;

import net.travelsystem.hotelservice.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
    Optional<Hotel> findByNameIgnoreCaseAndLocationIgnoreCase(String name,String location);
}
