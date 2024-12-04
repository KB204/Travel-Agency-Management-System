package net.travelsystem.hotelservice.dao;

import net.travelsystem.hotelservice.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel,Long>, JpaSpecificationExecutor<Hotel> {
    Optional<Hotel> findByNameIgnoreCaseAndLocationIgnoreCase(String name,String location);
}
