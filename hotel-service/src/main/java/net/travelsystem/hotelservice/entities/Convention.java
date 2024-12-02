package net.travelsystem.hotelservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Convention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identifier;
    private Integer availableRooms;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;
}
