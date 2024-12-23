package net.travelsystem.reservationservice.service;

import net.travelsystem.reservationservice.clients.FlightConventionRest;
import net.travelsystem.reservationservice.clients.HotelConventionRest;
import net.travelsystem.reservationservice.dao.ReservationRepository;
import net.travelsystem.reservationservice.dto.external_services.*;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.entities.Client;
import net.travelsystem.reservationservice.entities.Reservation;
import net.travelsystem.reservationservice.entities.Trip;
import net.travelsystem.reservationservice.enums.ReservationStatus;
import net.travelsystem.reservationservice.exceptions.ResourceNotFoundException;
import net.travelsystem.reservationservice.mapper.ReservationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private HotelConventionRest hotelConventionRest;
    @Mock
    private FlightConventionRest flightConventionRest;
    @Mock
    private ReservationMapper mapper;
    @InjectMocks
    private ReservationServiceImpl underTest;

    @Test
    void shouldGetAllReservations() {
        // given
        FlightResponse flightResponse = FlightResponse.builder().flightNo("conv2").build();
        HotelResponseDTO hotelResponse = new HotelResponseDTO("azure","azure");


        FlightConventionDTO flightConvention = new FlightConventionDTO(flightResponse);
        HotelConventionDTO hotelConvention = new HotelConventionDTO(hotelResponse);

        Trip trip = Trip.builder()
                .hotelConventionIdentifier("conv1")
                .flightConventionIdentifier(flightResponse.flightNo())
                .build();

        Reservation reservation = Reservation.builder()
                .identifier("test")
                .reservationDate(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .trip(trip)
                .build();

        Reservation reservation2 = Reservation.builder()
                .identifier("test2")
                .reservationDate(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .trip(trip)
                .build();

        List<Reservation> reservations = List.of(reservation,reservation2);

        ReservationResponse response = ReservationResponse.builder()
                .identifier("test")
                .reservationDate(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .build();

        ReservationResponse response2 = ReservationResponse.builder()
                .identifier("test2")
                .reservationDate(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .build();

        List<ReservationResponse> expectedResponses = List.of(response,response2);

        // when
        when(reservationRepository.findAll()).thenReturn(reservations);
        when(hotelConventionRest.findHotel(trip.getHotelConventionIdentifier())).thenReturn(hotelConvention);
        when(flightConventionRest.findFlight(trip.getFlightConventionIdentifier())).thenReturn(flightConvention);
        when(mapper.reservationToDtoResponse(reservation)).thenReturn(response);
        when(mapper.reservationToDtoResponse(reservation2)).thenReturn(response2);

        List<ReservationResponse> responses = underTest.getAllReservations();

        // then
        assertEquals(expectedResponses.size(),responses.size());
        assertEquals(expectedResponses.getFirst().identifier(), responses.getFirst().identifier());
        assertEquals(expectedResponses.get(1).identifier(),responses.get(1).identifier());
    }

    @Test
    void shouldCalculateReservationTotalAmount() {
        // given
        Client client = Client.builder().identity("test").nbrTickets(3).build();
        Trip trip = Trip.builder().price(500.0).build();

        Reservation reservation = Reservation.builder()
                .identifier("test")
                .reservationDate(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .trip(trip)
                .client(client)
                .build();

        // when
        when(reservationRepository.findByIdentifierIgnoreCase(reservation.getIdentifier())).thenReturn(Optional.of(reservation));
        double total = underTest.reservationTotalAmount(reservation.getIdentifier());

        // then
        assertEquals(1500.0,total);
        verify(reservationRepository).findByIdentifierIgnoreCase(reservation.getIdentifier());
    }

    @Test
    void shouldNotCalculateReservationTotalAmount() {
        // given
        Client client = Client.builder().identity("test").nbrTickets(3).build();
        Trip trip = Trip.builder().price(500.0).build();

        Reservation reservation = Reservation.builder()
                .identifier("test")
                .reservationDate(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .trip(trip)
                .client(client)
                .build();

        // when
        when(reservationRepository.findByIdentifierIgnoreCase(reservation.getIdentifier())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.reservationTotalAmount(reservation.getIdentifier()))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}