package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dto.hotel.HotelDetailsDTO;
import net.travelsystem.hotelservice.dto.room.RoomRequest;
import net.travelsystem.hotelservice.dto.room.RoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RoomService {
    Page<RoomResponse> findAllRooms(String roomNumber, String type, Double price, Pageable pageable);
    Page<RoomResponse> findAllAvailableRooms(String roomNumber, String type, Double price, Pageable pageable);
    void createRoom(RoomRequest request);
    HotelDetailsDTO hotelDetails(String roomNumber, String type, Double price, String name, String location, Pageable pageable);
}
