package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dto.hotel.HotelDetailsDTO;
import net.travelsystem.hotelservice.dto.room.RoomRequest;
import net.travelsystem.hotelservice.dto.room.RoomResponse;

import java.util.List;

public interface RoomService {
    List<RoomResponse> findAllRooms();
    void createRoom(RoomRequest request);
    HotelDetailsDTO hotelDetails(String name, String location);
}
