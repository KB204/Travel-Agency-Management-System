package ma.emsi.volservice.dao;

import ma.emsi.volservice.model.Convention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ConventionRepository extends JpaRepository<Convention,Long>, JpaSpecificationExecutor<Convention> {
    Optional<Convention> findByFlight_FlightNo(String flightNo);
}
