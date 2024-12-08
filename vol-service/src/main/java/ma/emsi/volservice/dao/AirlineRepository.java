package ma.emsi.volservice.dao;

import ma.emsi.volservice.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long>, JpaSpecificationExecutor<Airline> {
    Optional<Airline> findByNameIgnoreCaseAndIcaoCodeIgnoreCase(String name, String icaoCode);
}
