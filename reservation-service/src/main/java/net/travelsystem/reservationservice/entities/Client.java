package net.travelsystem.reservationservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identity;
    private String passportNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer nbrTickets;
    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
}
