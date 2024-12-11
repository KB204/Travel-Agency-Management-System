package ma.emsi.volservice.service;

import ma.emsi.volservice.dao.ConventionRepository;
import ma.emsi.volservice.dao.FlightRepository;
import ma.emsi.volservice.dto.convention.ConventionRequest;
import ma.emsi.volservice.dto.convention.ConventionResponse;
import ma.emsi.volservice.exceptions.FlightException;
import ma.emsi.volservice.exceptions.ResourceAlreadyExists;
import ma.emsi.volservice.exceptions.ResourceNotFoundException;
import ma.emsi.volservice.mapper.ConventionMapper;
import ma.emsi.volservice.model.Convention;
import ma.emsi.volservice.model.Flight;
import ma.emsi.volservice.service.specification.ConventionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ConventionServiceImpl implements ConventionService {
    private final ConventionRepository conventionRepository;
    private final FlightRepository flightRepository;
    private final ConventionMapper mapper;

    public ConventionServiceImpl(ConventionRepository conventionRepository, FlightRepository flightRepository, ConventionMapper mapper) {
        this.conventionRepository = conventionRepository;
        this.flightRepository = flightRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<ConventionResponse> getAllConventions(Integer nbr, String flightNo, String origin, String destination,
                                                      String depTime, String arrivalTime, Pageable pageable) {

        Specification<Convention> specification = ConventionSpecification.filterWithoutConditions()
                .and(ConventionSpecification.availablePlacesEqual(nbr))
                .and(ConventionSpecification.flightNoEqual(flightNo))
                .and(ConventionSpecification.flightOriginLike(origin))
                .and(ConventionSpecification.flightDestinationLike(destination))
                .and(ConventionSpecification.flightDepartureTimeLike(depTime))
                .and(ConventionSpecification.flightArrivalTimeLike(arrivalTime));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("flight.departureTime").descending());

        return conventionRepository.findAll(specification,pageable)
                .map(mapper::conventionToDtoResponse);
    }

    @Override
    public void createNewConvention(ConventionRequest request) {
        Flight flight = flightRepository.findByFlightNoIgnoreCase(request.flightNo())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Le vol identifié par %s n'existe pas",request.flightNo())));

        conventionRepository.findByFlight_FlightNo(request.flightNo())
                .ifPresent(convention -> {
                    throw new ResourceAlreadyExists("Convention exists déja");
                });

        Convention convention = mapper.requestDtoToConvention(request);
        convention.setFlight(flight);

        conventionRepository.save(convention);
    }

    @Override
    public ConventionResponse getConventionDetails(String flightNo) {
        Convention convention = conventionRepository.findByFlight_FlightNo(flightNo)
                .orElseThrow(() -> new FlightException(String.format("Le vol identifié par %s n'existe pas",flightNo)));

        return mapper.conventionToDtoResponse(convention);
    }
}
