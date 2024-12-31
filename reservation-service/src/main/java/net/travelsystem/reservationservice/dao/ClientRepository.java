package net.travelsystem.reservationservice.dao;

import net.travelsystem.reservationservice.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client,Long> {
    List<Client> findByIdentity(String identity);
}
