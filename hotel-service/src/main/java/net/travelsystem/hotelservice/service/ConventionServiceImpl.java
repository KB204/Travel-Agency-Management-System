package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dao.ConventionRepository;
import net.travelsystem.hotelservice.dao.HotelRepository;
import net.travelsystem.hotelservice.dto.convention.ConventionRequest;
import net.travelsystem.hotelservice.dto.convention.ConventionResponse;
import net.travelsystem.hotelservice.entities.Convention;
import net.travelsystem.hotelservice.entities.Hotel;
import net.travelsystem.hotelservice.entities.Room;
import net.travelsystem.hotelservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.hotelservice.exceptions.ResourceNotFoundException;
import net.travelsystem.hotelservice.mapper.ConventionMapper;
import net.travelsystem.hotelservice.service.specification.ConventionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ConventionServiceImpl implements ConventionService {
    private final ConventionRepository conventionRepository;
    private final HotelRepository hotelRepository;
    private final ConventionMapper mapper;

    public ConventionServiceImpl(ConventionRepository conventionRepository, HotelRepository hotelRepository, ConventionMapper mapper) {
        this.conventionRepository = conventionRepository;
        this.hotelRepository = hotelRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<ConventionResponse> getAllConventions(String identifier, Integer nbr, String checkIn, String checkOut,
                                                      LocalDate start, LocalDate end, Pageable pageable) {

        Specification<Convention> specification = ConventionSpecification.filterWithoutConditions()
                .and(ConventionSpecification.identifierEqual(identifier))
                .and(ConventionSpecification.availableRoomsEqual(nbr))
                .and(ConventionSpecification.checkInDateLike(checkIn))
                .and(ConventionSpecification.checkOutDateLike(checkOut))
                .and(ConventionSpecification.conventionDateBetween(start, end));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("checkInDate").descending());

        return conventionRepository.findAll(specification,pageable)
                .map(mapper::conventionToDtoResponse);
    }

    @Override
    public void createHotelConvention(ConventionRequest request) {
        Hotel hotel = hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(request.name(), request.location())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("L'hotel %s situé a %s n'existe pas",request.name(),request.location())));

        Convention convention = Convention.builder()
                .identifier(UUID.randomUUID().toString().substring(0,8))
                .checkInDate(request.checkInDate())
                .checkOutDate(request.checkOutDate())
                .hotel(hotel)
                .build();

        conventionRepository.findByIdentifierIgnoreCase(convention.getIdentifier())
                .ifPresent(co -> {
                    throw new ResourceAlreadyExists(String.format("La convention identifié par %s exists déja",
                            co.getIdentifier()));
                });

        Integer rooms = calculateAvailableRooms(hotel);
        convention.setAvailableRooms(rooms);

        conventionRepository.save(convention);
    }

    private Integer calculateAvailableRooms(Hotel hotel){
        return Optional.ofNullable(hotel)
                .map(Hotel::getRooms)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Room::getAvailable)
                .mapToInt(room -> 1)
                .sum();
    }
}
