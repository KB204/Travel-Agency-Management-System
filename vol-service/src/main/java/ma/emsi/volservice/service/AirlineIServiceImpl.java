package ma.emsi.volservice.service;

import ma.emsi.volservice.dao.AirlineRepository;
import ma.emsi.volservice.dto.airline.AirlineRequest;
import ma.emsi.volservice.dto.airline.AirlineResponse;
import ma.emsi.volservice.exceptions.ResourceAlreadyExists;
import ma.emsi.volservice.exceptions.ResourceNotFoundException;
import ma.emsi.volservice.mapper.AirlineMapper;
import ma.emsi.volservice.model.Airline;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirlineIServiceImpl implements AirlineIService {
    private final AirlineRepository airlineRepository;
    private final AirlineMapper mapper;

    public AirlineIServiceImpl(AirlineRepository airlineRepository, AirlineMapper mapper) {
        this.airlineRepository = airlineRepository;
        this.mapper = mapper;
    }


    @Override
    public List<AirlineResponse> getAllAirline() {
        return airlineRepository.findAll()
                .stream()
                .map(mapper::airlineToDtoResponse)
                .toList();
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
