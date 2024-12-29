package ma.emsi.volservice.service;

import ma.emsi.volservice.dao.AirlineRepository;
import ma.emsi.volservice.dao.FlightRepository;
import ma.emsi.volservice.dto.flight.FlightRequest;
import ma.emsi.volservice.dto.flight.FlightResponse;
import ma.emsi.volservice.exceptions.FlightException;
import ma.emsi.volservice.exceptions.ResourceAlreadyExists;
import ma.emsi.volservice.exceptions.ResourceNotFoundException;
import ma.emsi.volservice.mapper.FlightMapper;
import ma.emsi.volservice.model.Airline;
import ma.emsi.volservice.model.Flight;
import ma.emsi.volservice.service.specification.FlightSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class FlightIServiceImpl implements FlightIService {
    private final FlightRepository flightRepository;
    private final FlightMapper mapper;
    private final AirlineRepository airlineRepository;

    public FlightIServiceImpl(FlightRepository flightRepository, FlightMapper mapper, AirlineRepository airlineRepository) {
        this.flightRepository = flightRepository;
        this.mapper = mapper;
        this.airlineRepository = airlineRepository;
    }

    @Override
    public Page<FlightResponse> getAllFlights(String flightNo, String type, String origin, String destination, String airline, String depDate, Pageable pageable) {

        Specification<Flight> specification = FlightSpecification.filterWithoutConditions()
                .and(FlightSpecification.flightNoEqual(flightNo))
                .and(FlightSpecification.flightTypeEqual(type))
                .and(FlightSpecification.flightOriginLike(origin))
                .and(FlightSpecification.flightDestinationLike(destination))
                .and(FlightSpecification.flightAirlineLike(airline))
                .and(FlightSpecification.flightDepartureTimeLike(depDate));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("departureTime").descending());

        return flightRepository.findAll(specification,pageable)
                .map(mapper::flightToDtoResponse);
    }

    @Override
    public void addFlight(FlightRequest flightRequest) {
        Airline airline = airlineRepository.findByNameIgnoreCaseAndCodeIgnoreCase(flightRequest.name(),flightRequest.code())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Compagnie aérienne %s n'existe pas !", flightRequest.name())));

        Flight flight = mapper.dtoRequestToFlight(flightRequest);
        flightRepository.findByFlightNoIgnoreCase(flight.getFlightNo())
                .ifPresent(a->{
                    throw new ResourceAlreadyExists(String.format("Le vol %s exists déja", flight.getFlightNo()));
                });

        checkRules(flight);
        flight.setFlightNo(UUID.randomUUID().toString().substring(0,10));
        flight.setAirline(airline);
        flightRepository.save(flight);
    }
    private void checkRules(Flight flight){
        if (flight.getArrivalTime().isBefore(flight.getDepartureTime()))
            throw new FlightException("La date d'arrivée ne peut pas être inférieure à la date de départ");
        if (flight.getArrivalTime().isEqual(flight.getDepartureTime()))
            throw new FlightException("La date d'arrivée ne peut pas être la même que la date de départ");
        if (flight.getDepartureTime().isBefore(LocalDateTime.now()))
            throw new FlightException("La date de départ n'est pas valide");
    }
}
