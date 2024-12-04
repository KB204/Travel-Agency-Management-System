package net.travelsystem.hotelservice.dao;

import net.travelsystem.hotelservice.entities.Convention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ConventionRepository extends JpaRepository<Convention,Long>, JpaSpecificationExecutor<Convention> {
    Optional<Convention> findByIdentifierIgnoreCase(String identifier);
}
