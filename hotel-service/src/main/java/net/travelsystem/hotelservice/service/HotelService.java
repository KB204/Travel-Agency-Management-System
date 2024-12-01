package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dto.hotel.HotelRequest;
import net.travelsystem.hotelservice.dto.hotel.HotelResponse;

import java.util.List;

public interface HotelService {
    List<HotelResponse> findAllHotels();
    void createNewHotel(HotelRequest request);
    void updateHotel(Long id,HotelRequest request);
    void deleteHotel(Long id);
}
