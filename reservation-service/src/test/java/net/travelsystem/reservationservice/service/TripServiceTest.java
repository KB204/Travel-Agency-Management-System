package net.travelsystem.reservationservice.service;

import net.travelsystem.reservationservice.clients.HotelConventionRest;
import net.travelsystem.reservationservice.dao.TripRepository;
import net.travelsystem.reservationservice.dto.external_services.FlightConvention;
import net.travelsystem.reservationservice.dto.external_services.HotelConvention;
import net.travelsystem.reservationservice.dto.trip.TripRequest;
import net.travelsystem.reservationservice.dto.trip.TripUpdateRequest;
import net.travelsystem.reservationservice.entities.Reservation;
import net.travelsystem.reservationservice.entities.Trip;
import net.travelsystem.reservationservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.reservationservice.exceptions.ResourceNotFoundException;
import net.travelsystem.reservationservice.mapper.TripMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {
    @Mock
    private TripRepository repository;
    @Mock
    private HotelConventionRest rest;
    @Mock
    private TripMapper mapper;
    @InjectMocks
    private TripServiceImpl underTest;

    @Test
    void shouldCreateNewTripWhenConventionsAreUnique() {
        // given
        List<Reservation> reservations = new ArrayList<>();
        FlightConvention flightConvention = new FlightConvention();
        HotelConvention hotelConvention = HotelConvention.builder().identifier("test").build();

        Trip trip = new Trip(1L,"trip", "new trip",5000.0,"rabat","dubai",5,5,hotelConvention.identifier(),"test2", reservations,hotelConvention,flightConvention);
        TripRequest request = new TripRequest(hotelConvention.identifier(),"test2","trip", "new trip",5000.0,"rabat","dubai",5,5 );

        // when
        when(rest.getHotelConventionDetails(request.hotelConventionIdentifier())).thenReturn(hotelConvention);
        when(mapper.dtoRequestToTrip(request)).thenReturn(trip);
        when(repository.findByHotelConventionIdentifierOrFlightConventionIdentifier(request.hotelConventionIdentifier(),
                request.flightConventionIdentifier())).thenReturn(Optional.empty());

        underTest.createTrip(request);

        // then
        verify(repository).save(trip);
        assertThat(trip).isNotNull();
        assertThat(trip.getName()).isEqualTo(request.name());
        assertThat(trip.getDescription()).isEqualTo(request.description());
        assertThat(trip.getPrice()).isEqualTo(request.price());
        assertThat(trip.getOrigin()).isEqualTo(request.origin());
        assertThat(trip.getDestination()).isEqualTo(request.destination());
        assertThat(trip.getDuration()).isEqualTo(request.duration());
        assertThat(trip.getAvailablePlaces()).isEqualTo(request.availablePlaces());
        assertThat(trip.getHotelConventionIdentifier()).isEqualTo(request.hotelConventionIdentifier());
        assertThat(trip.getFlightConventionIdentifier()).isEqualTo(request.flightConventionIdentifier());
    }
    @Test
    void shouldNotCreateNewTrip() {
        // given
        List<Reservation> reservations = new ArrayList<>();
        FlightConvention flightConvention = new FlightConvention();
        HotelConvention hotelConvention = HotelConvention.builder().identifier("test").build();

        Trip trip = new Trip(1L,"trip", "new trip",5000.0,"rabat","dubai",5,5,hotelConvention.identifier(),"test2", reservations,hotelConvention,flightConvention);
        TripRequest request = new TripRequest(hotelConvention.identifier(),"test2","trip", "new trip",5000.0,"rabat","dubai",5,5 );

        // when
        when(repository.findByHotelConventionIdentifierOrFlightConventionIdentifier(request.hotelConventionIdentifier(),
                request.flightConventionIdentifier())).thenReturn(Optional.of(trip));

        // then
        assertThatThrownBy(() -> underTest.createTrip(request))
                .isInstanceOf(ResourceAlreadyExists.class);
        verify(repository,never()).save(trip);
    }

    @Test
    void shouldUpdateExistingTrip() {
        // given
        List<Reservation> reservations = new ArrayList<>();
        FlightConvention flightConvention = new FlightConvention();
        HotelConvention hotelConvention = HotelConvention.builder().identifier("test").build();

        Trip trip = new Trip(1L,"trip", "new trip",5000.0,"rabat","dubai",5,5,hotelConvention.identifier(),"test2", reservations,hotelConvention,flightConvention);
        TripUpdateRequest request = new TripUpdateRequest("trip", "new trip",5000.0,"rabat","dubai",5,5 );

        // when
        when(repository.findById(trip.getId())).thenReturn(Optional.of(trip));
        underTest.updateTrip(trip.getId(),request);

        // then
        verify(repository).save(trip);
        assertThat(trip).isNotNull();
        assertThat(trip.getName()).isEqualTo(request.name());
        assertThat(trip.getDescription()).isEqualTo(request.description());
        assertThat(trip.getPrice()).isEqualTo(request.price());
        assertThat(trip.getOrigin()).isEqualTo(request.origin());
        assertThat(trip.getDestination()).isEqualTo(request.destination());
        assertThat(trip.getDuration()).isEqualTo(request.duration());
        assertThat(trip.getAvailablePlaces()).isEqualTo(request.availablePlaces());

    }
    @Test
    void shouldNotUpdateExistingTrip() {
        // given
        List<Reservation> reservations = new ArrayList<>();
        FlightConvention flightConvention = new FlightConvention();
        HotelConvention hotelConvention = HotelConvention.builder().identifier("test").build();

        Trip trip = new Trip(1L,"trip", "new trip",5000.0,"rabat","dubai",5,5,hotelConvention.identifier(),"test2", reservations,hotelConvention,flightConvention);
        TripUpdateRequest request = new TripUpdateRequest("trip", "new trip",5000.0,"rabat","dubai",5,5 );

        // when
        when(repository.findById(trip.getId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.updateTrip(trip.getId(), request))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository,never()).save(trip);

    }
}