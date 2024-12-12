package net.travelsystem.reservationservice.dao;

import net.travelsystem.reservationservice.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
