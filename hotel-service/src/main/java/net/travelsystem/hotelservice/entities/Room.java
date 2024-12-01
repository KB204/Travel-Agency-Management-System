package net.travelsystem.hotelservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travelsystem.hotelservice.enums.RoomType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomNumber;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private Double price;
    private Boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;
}
