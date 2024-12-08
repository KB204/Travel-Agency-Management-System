package ma.emsi.volservice.service;

import ma.emsi.volservice.dao.AirlineRepository;
import ma.emsi.volservice.dto.airline.AirlineRequest;
import ma.emsi.volservice.dto.airline.AirlineResponse;
import ma.emsi.volservice.exceptions.ResourceAlreadyExists;
import ma.emsi.volservice.exceptions.ResourceNotFound;
import ma.emsi.volservice.model.Airline;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirlineIServiceImpl implements AirlineIService {
    private final AirlineRepository airlineRepository;
    private final ModelMapper modelMapper;

    public AirlineIServiceImpl(AirlineRepository airlineRepository, ModelMapper modelMapper) {
        this.airlineRepository = airlineRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<AirlineResponse> getAllAirline() {
        return airlineRepository.findAll().stream()
                .map(airline -> modelMapper.map(airline, AirlineResponse.class)).collect(Collectors.toList());
    }

    @Override
    public void createAirline(AirlineRequest airlineRequest) {
        Airline airline = modelMapper.map(airlineRequest, Airline.class);
        airlineRepository.findByNameIgnoreCaseAndIcaoCodeIgnoreCase(airlineRequest.getName(),airlineRequest.getIcaoCode())
                .ifPresent(a -> {
                    throw new ResourceAlreadyExists(String.format("Airline %s avec le code icao %s exists déja",airlineRequest.getName(),airlineRequest.getIcaoCode()));
                });
        airlineRepository.save(airline);
    }

    @Override
    public void updateAirline(Long id, AirlineRequest airlineRequest) {
        Airline airlineFound = airlineRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Airline non trouvé"));
        modelMapper.map(airlineRequest, airlineFound);
        airlineRepository.save(airlineFound);
    }

    @Override
    public void deleteAirline(Long airlineId) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(()-> new ResourceNotFound("Airline non trouvé"));
        airlineRepository.delete(airline);
    }
}
