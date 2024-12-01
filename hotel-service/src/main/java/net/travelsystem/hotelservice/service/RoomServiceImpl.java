package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dao.HotelRepository;
import net.travelsystem.hotelservice.dao.RoomRepository;
import net.travelsystem.hotelservice.dto.hotel.HotelDetailsDTO;
import net.travelsystem.hotelservice.dto.room.RoomRequest;
import net.travelsystem.hotelservice.dto.room.RoomResponse;
import net.travelsystem.hotelservice.dto.room.RoomResponseDTO;
import net.travelsystem.hotelservice.entities.Hotel;
import net.travelsystem.hotelservice.entities.Room;
import net.travelsystem.hotelservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.hotelservice.exceptions.ResourceNotFoundException;
import net.travelsystem.hotelservice.mapper.RoomMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper mapper;

    public RoomServiceImpl(RoomRepository roomRepository, HotelRepository hotelRepository, RoomMapper mapper) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.mapper = mapper;
    }

    @Override
    public List<RoomResponse> findAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(mapper::roomToDtoResponse)
                .toList();
    }

    @Override
    public void createRoom(RoomRequest request) {
        Hotel hotel = hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(request.name(), request.location())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("L'hotel %s situé a %s n'existe pas",request.name(),request.location())));

        roomRepository.findByRoomNumberIgnoreCaseAndHotel_NameAndHotel_Location(request.roomNumber(), hotel.getName(), hotel.getLocation())
                .ifPresent(room -> {
                    throw new ResourceAlreadyExists(String.format("La Chambre numéro %s existe déja",request.roomNumber()));
                });

        Room room = Room.builder()
                .roomNumber(request.roomNumber())
                .price(request.price())
                .roomType(request.roomType())
                .available(request.available())
                .hotel(hotel)
                .build();

        roomRepository.save(room);
    }

    @Override
    public HotelDetailsDTO hotelDetails(String name, String location) {
        Hotel hotel = hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(name, location)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("L'hotel %s situé a %s n'existe pas",name,location)));

        List<Room> rooms = roomRepository.findByHotelNameAndHotelLocation(hotel.getName(), hotel.getLocation());
        List<RoomResponseDTO> roomResponses = rooms
                .stream()
                .map(mapper::roomToDetailsResponse)
                .toList();

        return new HotelDetailsDTO(hotel.getName(), hotel.getLocation(), roomResponses);
    }
}
