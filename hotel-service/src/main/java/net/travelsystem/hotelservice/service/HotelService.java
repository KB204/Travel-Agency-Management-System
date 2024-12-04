package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dto.hotel.HotelRequest;
import net.travelsystem.hotelservice.dto.hotel.HotelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HotelService {
    Page<HotelResponse> findAllHotels(String name, String location, Pageable pageable);
    void createNewHotel(HotelRequest request);
    void updateHotel(Long id,HotelRequest request);
    void deleteHotel(Long id);
}
