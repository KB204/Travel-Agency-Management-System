package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dao.HotelRepository;
import net.travelsystem.hotelservice.dto.hotel.HotelRequest;
import net.travelsystem.hotelservice.dto.hotel.HotelResponse;
import net.travelsystem.hotelservice.entities.Hotel;
import net.travelsystem.hotelservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.hotelservice.exceptions.ResourceNotFoundException;
import net.travelsystem.hotelservice.mapper.HotelMapper;
import net.travelsystem.hotelservice.service.specification.HotelSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper mapper;

    public HotelServiceImpl(HotelRepository hotelRepository, HotelMapper mapper) {
        this.hotelRepository = hotelRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<HotelResponse> findAllHotels(String name, String location, Pageable pageable) {

        Specification<Hotel> specification = HotelSpecification.filterWithoutConditions()
                .and(HotelSpecification.hotelNameLike(name))
                .and(HotelSpecification.hotelLocationLike(location));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name").ascending());

        return hotelRepository.findAll(specification,pageable)
                .map(mapper::hotelToDtoResponse);
    }

    @Override
    public void createNewHotel(HotelRequest request) {
        hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(request.name(), request.location())
                .ifPresent(hotel -> {
                    throw new ResourceAlreadyExists(String.format("Hotel %s situé a %s exists déja",request.name(),request.location()));
                });

        Hotel hotel = mapper.dtoRequestToHotel(request);
        hotelRepository.save(hotel);
    }

    @Override
    public void updateHotel(Long id,HotelRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel non trouvé"));

        hotel.setName(request.name());
        hotel.setLocation(request.location());
        hotel.setContact(request.contact());

        hotelRepository.save(hotel);

    }

    @Override
    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel non trouvé"));

        hotelRepository.delete(hotel);
    }
}
