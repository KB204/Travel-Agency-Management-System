package net.travelsystem.hotelservice.dto.hotel;

public record HotelResponse(
        Long id,
        String name,
        String location,
        String contact) {}
