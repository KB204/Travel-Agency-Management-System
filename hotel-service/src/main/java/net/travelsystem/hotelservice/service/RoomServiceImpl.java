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
import net.travelsystem.hotelservice.service.specification.RoomSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<RoomResponse> findAllRooms(String roomNumber, String type, Double price, Pageable pageable) {

        Specification<Room> specification = RoomSpecification.filterWithoutConditions()
                .and(RoomSpecification.roomNumberEqual(roomNumber))
                .and(RoomSpecification.roomTypeEqual(type))
                .and(RoomSpecification.roomPriceEqual(price));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending());

        return roomRepository.findAll(specification,pageable)
                .map(mapper::roomToDtoResponse);
    }

    @Override
    public Page<RoomResponse> findAllAvailableRooms(String roomNumber, String type, Double price,Pageable pageable) {

        Specification<Room> specification = Specification.where(RoomSpecification.onlyParamRooms(true))
                .and(RoomSpecification.roomNumberEqual(roomNumber))
                .and(RoomSpecification.roomTypeEqual(type))
                .and(RoomSpecification.roomPriceEqual(price));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending());

        return roomRepository.findAll(specification,pageable)
                .map(mapper::roomToDtoResponse);
    }

    @Override
    public void createRoom(RoomRequest request) {
        Hotel hotel = hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(request.name(), request.location())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("L'hotel %s situé a %s n'existe pas",request.name(),request.location())));

        roomRepository.findByRoomNumberIgnoreCaseAndHotel_NameAndHotel_Location(request.roomNumber(), hotel.getName(), hotel.getLocation())
                .ifPresent(room -> {
                    throw new ResourceAlreadyExists(String.format("La Chambre numéro %s existe déja",request.roomNumber()));
                });

        Room room = mapper.dtoRequestToRoom(request);
        room.setHotel(hotel);

        roomRepository.save(room);
    }

    @Override
    public HotelDetailsDTO hotelDetails(String roomNumber, String type, Double price, String name, String location,Pageable pageable) {
        Hotel hotel = hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(name, location)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("L'hotel %s situé a %s n'existe pas",name,location)));

        Specification<Room> specification = Specification.where(RoomSpecification.onlySpecificHotel(name, location))
                .and(RoomSpecification.roomNumberEqual(roomNumber))
                .and(RoomSpecification.roomTypeEqual(type))
                .and(RoomSpecification.roomPriceEqual(price));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending());

        Page<Room> rooms = roomRepository.findAll(specification,pageable);
        List<RoomResponseDTO> roomResponses = rooms
                .stream()
                .map(mapper::roomToDetailsResponse)
                .toList();
        return new HotelDetailsDTO(hotel.getName(), hotel.getLocation(), roomResponses);
    }
}
