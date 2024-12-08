package ma.emsi.volservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.volservice.enums.FlightType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String flightNo;
    private String origin;
    private String destination;
    @Enumerated(EnumType.STRING)
    private FlightType flightType;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private Airline airline;
}
