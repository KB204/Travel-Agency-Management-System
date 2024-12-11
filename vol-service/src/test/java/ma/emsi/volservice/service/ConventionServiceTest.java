package ma.emsi.volservice.service;

import ma.emsi.volservice.dao.ConventionRepository;
import ma.emsi.volservice.dao.FlightRepository;
import ma.emsi.volservice.dto.airline.AirlineResponseDTO;
import ma.emsi.volservice.dto.convention.ConventionRequest;
import ma.emsi.volservice.dto.convention.ConventionResponse;
import ma.emsi.volservice.dto.flight.FlightResponse;
import ma.emsi.volservice.enums.FlightType;
import ma.emsi.volservice.exceptions.FlightException;
import ma.emsi.volservice.exceptions.ResourceAlreadyExists;
import ma.emsi.volservice.exceptions.ResourceNotFoundException;
import ma.emsi.volservice.mapper.ConventionMapper;
import ma.emsi.volservice.model.Convention;
import ma.emsi.volservice.model.Flight;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConventionServiceTest {
    @Mock
    private ConventionRepository conventionRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private ConventionMapper mapper;
    @InjectMocks
    private ConventionServiceImpl underTest;

    @Test
    void shouldCreateNewConventionWhenFlightExistsAndConventionNotAlreadyExists() {
        // given
        Flight flight = new Flight();
        Convention convention = new Convention(1L,10,flight);
        ConventionRequest request = new ConventionRequest(flight.getFlightNo(), 10);

        // when
        when(flightRepository.findByFlightNoIgnoreCase(request.flightNo())).thenReturn(Optional.of(flight));
        when(conventionRepository.findByFlight_FlightNo(request.flightNo())).thenReturn(Optional.empty());
        when(mapper.requestDtoToConvention(request)).thenReturn(convention);

        underTest.createNewConvention(request);

        // then
        verify(conventionRepository).save(convention);
        assertThat(convention.getAvailablePlaces()).isEqualTo(request.availablePlaces());
        assertThat(convention.getFlight().getFlightNo()).isEqualTo(request.flightNo());
    }

    @Test
    void shouldNotCreateConventionWhenFlightDoesntExists() {
        // given
        Flight flight = new Flight();
        Convention convention = new Convention(1L,10,flight);
        ConventionRequest request = new ConventionRequest(flight.getFlightNo(), 10);

        // when
        when(flightRepository.findByFlightNoIgnoreCase(request.flightNo())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.createNewConvention(request))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(conventionRepository,never()).save(convention);
    }

    @Test
    void shouldNotCreateConventionWhenConventionAlreadyExists() {
        // given
        Flight flight = new Flight();
        Convention convention = new Convention(1L,10,flight);
        ConventionRequest request = new ConventionRequest(flight.getFlightNo(), 10);

        // when
        when(flightRepository.findByFlightNoIgnoreCase(request.flightNo())).thenReturn(Optional.of(flight));
        when(conventionRepository.findByFlight_FlightNo(request.flightNo())).thenReturn(Optional.of(convention));

        // then
        assertThatThrownBy(() -> underTest.createNewConvention(request))
                .isInstanceOf(ResourceAlreadyExists.class);
        verify(conventionRepository,never()).save(convention);
    }

    @Test
    void shouldGetConventionDetails() {
        // given
        Flight flight = new Flight();
        FlightResponse flightResponse = new FlightResponse(flight.getFlightNo(), flight.getOrigin(), flight.getDestination(), FlightType.BUSINESS, LocalDateTime.now(),LocalDateTime.now(), new AirlineResponseDTO("test","test"));
        Convention convention = new Convention(1L,10,flight);
        ConventionResponse response = new ConventionResponse(convention.getAvailablePlaces(),flightResponse);

        // when
        when(conventionRepository.findByFlight_FlightNo(flight.getFlightNo())).thenReturn(Optional.of(convention));
        when(mapper.conventionToDtoResponse(convention)).thenReturn(response);

        ConventionResponse expectedResult = underTest.getConventionDetails(convention.getFlight().getFlightNo());

        // then
        assertThat(expectedResult).isNotNull();
        assertThat(expectedResult.availablePlaces()).isEqualTo(response.availablePlaces());
        assertThat(expectedResult.flight().flightNo()).isEqualTo(response.flight().flightNo());
    }

    @Test
    void shouldNotGetConvention() {
        // given
        Flight flight = new Flight();
        Convention convention = new Convention(1L,10,flight);

        // when
        when(conventionRepository.findByFlight_FlightNo(flight.getFlightNo())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.getConventionDetails(convention.getFlight().getFlightNo()))
                .isInstanceOf(FlightException.class);
    }
}