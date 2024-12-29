package ma.emsi.volservice.service;

import ma.emsi.volservice.dao.AirlineRepository;
import ma.emsi.volservice.dto.airline.AirlineRequest;
import ma.emsi.volservice.dto.airline.AirlineResponse;
import ma.emsi.volservice.exceptions.ResourceAlreadyExists;
import ma.emsi.volservice.exceptions.ResourceNotFoundException;
import ma.emsi.volservice.mapper.AirlineMapper;
import ma.emsi.volservice.model.Airline;
import ma.emsi.volservice.service.specification.AirlineSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class AirlineIServiceImpl implements AirlineIService {
    private final AirlineRepository airlineRepository;
    private final AirlineMapper mapper;

    public AirlineIServiceImpl(AirlineRepository airlineRepository, AirlineMapper mapper) {
        this.airlineRepository = airlineRepository;
        this.mapper = mapper;
    }


    @Override
    public Page<AirlineResponse> getAllAirline(String name, String code, Integer max, Pageable pageable) {

        Specification<Airline> specification = AirlineSpecification.filterWithoutConditions()
                .and(AirlineSpecification.airlineNameLike(name))
                .and(AirlineSpecification.airlineCodeEqual(code))
                .and(AirlineSpecification.airlineCapacityEqual(max));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name").ascending());

        return airlineRepository.findAll(specification,pageable)
                .map(mapper::airlineToDtoResponse);
    }

    @Override
    public void createAirline(AirlineRequest airlineRequest) {
        Airline airline = mapper.requestDtoToAirline(airlineRequest);
        airlineRepository.findByNameIgnoreCaseAndCodeIgnoreCase(airlineRequest.name(),airlineRequest.code())
                .ifPresent(a -> {
                    throw new ResourceAlreadyExists(String.format("Compagnie aérienne %s avec le code %s exists déja",airlineRequest.name(),airlineRequest.code()));
                });

        airlineRepository.save(airline);
    }

    @Override
    public void updateAirline(Long id, AirlineRequest airlineRequest) {
        Airline airlineFound = airlineRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Airline non trouvé"));

        airlineFound.setName(airlineRequest.name());
        airlineFound.setCode(airlineRequest.code());
        airlineFound.setHeadquarters(airlineRequest.headquarters());
        airlineFound.setFleetSize(airlineRequest.fleetSize());

        airlineRepository.save(airlineFound);
    }

    @Override
    public void deleteAirline(Long id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Airline non trouvé"));
        airlineRepository.delete(airline);
    }
}
