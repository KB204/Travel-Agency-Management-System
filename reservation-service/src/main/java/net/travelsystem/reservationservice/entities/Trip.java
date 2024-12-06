package net.travelsystem.reservationservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travelsystem.reservationservice.dto.external_services.FlightConvention;
import net.travelsystem.reservationservice.dto.external_services.HotelConvention;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String origin;
    private String destination;
    private Integer duration;
    private Integer availablePlaces;
    private String hotelConventionIdentifier;
    private String flightConventionIdentifier;
    @OneToMany(mappedBy = "trip",cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
    @Transient
    private HotelConvention hotelConvention;
    @Transient
    private FlightConvention flightConvention;
}
