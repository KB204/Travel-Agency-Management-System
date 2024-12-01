package net.travelsystem.hotelservice.dto.room;

import net.travelsystem.hotelservice.enums.RoomType;

public record RoomResponseDTO(
        String roomNumber,
        RoomType roomType,
        Double price,
        Boolean available) {}
