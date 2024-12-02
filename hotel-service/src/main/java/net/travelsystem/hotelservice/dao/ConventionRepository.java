package net.travelsystem.hotelservice.dao;

import net.travelsystem.hotelservice.entities.Convention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConventionRepository extends JpaRepository<Convention,Long> {
    Optional<Convention> findByIdentifierIgnoreCase(String identifier);
}
