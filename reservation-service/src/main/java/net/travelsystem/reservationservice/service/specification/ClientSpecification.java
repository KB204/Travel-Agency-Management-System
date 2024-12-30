package net.travelsystem.reservationservice.service.specification;

import net.travelsystem.reservationservice.entities.Client;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ClientSpecification {
    public static Specification<Client> filterClients() {
        return (root, query, criteriaBuilder) -> {
            Objects.requireNonNull(query).distinct(true);
            return criteriaBuilder.conjunction();
        };
    }
}
