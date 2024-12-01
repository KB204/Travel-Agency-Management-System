package net.travelsystem.hotelservice.dto.hotel;

import net.travelsystem.hotelservice.dto.room.RoomResponseDTO;

import java.util.List;

public record HotelDetailsDTO(
        String name,
        String location,
        List<RoomResponseDTO> rooms) {}
