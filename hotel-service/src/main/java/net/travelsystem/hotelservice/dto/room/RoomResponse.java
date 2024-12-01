package net.travelsystem.hotelservice.dto.room;

import net.travelsystem.hotelservice.dto.hotel.HotelResponseDTO;
import net.travelsystem.hotelservice.enums.RoomType;

public record RoomResponse(
        String roomNumber,
        RoomType roomType,
        Double price,
        Boolean available,
        HotelResponseDTO hotel) {}
