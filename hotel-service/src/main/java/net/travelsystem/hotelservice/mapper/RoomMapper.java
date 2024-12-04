package net.travelsystem.hotelservice.mapper;

import net.travelsystem.hotelservice.dto.room.RoomRequest;
import net.travelsystem.hotelservice.dto.room.RoomResponse;
import net.travelsystem.hotelservice.dto.room.RoomResponseDTO;
import net.travelsystem.hotelservice.entities.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomResponse roomToDtoResponse(Room room);
    RoomResponseDTO roomToDetailsResponse(Room room);
    Room dtoRequestToRoom(RoomRequest request);
}
