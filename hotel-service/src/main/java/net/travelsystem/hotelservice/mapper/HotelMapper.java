package net.travelsystem.hotelservice.mapper;

import net.travelsystem.hotelservice.dto.hotel.HotelResponse;
import net.travelsystem.hotelservice.entities.Hotel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelResponse hotelToDtoResponse(Hotel hotel);
}
