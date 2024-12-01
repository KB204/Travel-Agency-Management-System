package net.travelsystem.hotelservice.dao;

import net.travelsystem.hotelservice.entities.Convention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConventionRepository extends JpaRepository<Convention,Long> {
}
