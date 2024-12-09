package ma.emsi.volservice.dao;

import ma.emsi.volservice.model.Convention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConventionRepository extends JpaRepository<Convention,Long> {
    Optional<Convention> findByFlight_FlightNo(String flightNo);
}
