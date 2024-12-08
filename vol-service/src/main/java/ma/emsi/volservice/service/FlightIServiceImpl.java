package ma.emsi.volservice.service;

import ma.emsi.volservice.dao.AirlineRepository;
import ma.emsi.volservice.dao.FlightRepository;
import ma.emsi.volservice.dto.flight.FlightRequest;
import ma.emsi.volservice.dto.flight.FlightResponse;
import ma.emsi.volservice.exceptions.ResourceAlreadyExists;
import ma.emsi.volservice.exceptions.ResourceNotFound;
import ma.emsi.volservice.model.Airline;
import ma.emsi.volservice.model.Flight;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightIServiceImpl implements FlightIService {
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;
    private final AirlineRepository airlineRepository;

    public FlightIServiceImpl(FlightRepository flightRepository, ModelMapper modelMapper, AirlineRepository airlineRepository) {
        this.flightRepository = flightRepository;
        this.modelMapper = modelMapper;
        this.airlineRepository = airlineRepository;
    }

    @Override
    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(flight -> modelMapper.map(flight, FlightResponse.class)).collect(Collectors.toList());
    }

    @Override
    public void addFlight(FlightRequest flightRequest) {
        Airline airline = airlineRepository.findByNameIgnoreCaseAndIcaoCodeIgnoreCase(flightRequest.getAirlineName(),flightRequest.getIcaoCode()).
                orElseThrow(() -> new ResourceNotFound(String.format("L'airline %s n'existe pas !", flightRequest.getAirlineName())));

        flightRepository.findByFlightNoIgnoreCaseAndOriginIgnoreCase(flightRequest.getFlightNo(), flightRequest.getOrigin())
                .ifPresent(a->{
                    throw new ResourceAlreadyExists(String.format("Flight %s from %s already exists", flightRequest.getFlightNo(), flightRequest.getOrigin()));
                });
        Flight flight = modelMapper.map(flightRequest, Flight.class);
        flight.setAirline(airline);
        flightRepository.save(flight);
    }
}
