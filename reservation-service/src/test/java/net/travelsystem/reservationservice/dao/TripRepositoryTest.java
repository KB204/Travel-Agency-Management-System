package net.travelsystem.reservationservice.dao;

import net.travelsystem.reservationservice.dto.external_services.FlightConvention;
import net.travelsystem.reservationservice.dto.external_services.FlightResponse;
import net.travelsystem.reservationservice.dto.external_services.HotelConvention;
import net.travelsystem.reservationservice.entities.Reservation;
import net.travelsystem.reservationservice.entities.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TripRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private TripRepository repository;

    List<Trip> trips = new ArrayList<>();
    List<Reservation> reservations = new ArrayList<>();
    FlightConvention flightConvention = new FlightConvention(10, FlightResponse.builder().build());
    HotelConvention hotelConvention = HotelConvention.builder().build();

    @BeforeEach
    void setUp() {
        System.out.println("-----------------------------------------------");
        this.trips = List.of(
                new Trip(null,"trip1","desc trip",85000.0,"casa","new york",3,
                        50,"test","test1",reservations,hotelConvention,flightConvention),
                new Trip(null,"trip2","desc trip2",95000.0,"casa","paris",5,
                        500,"test2","test3",reservations,hotelConvention,flightConvention)
        );
        repository.saveAll(trips);
        System.out.println("------------------------------------------------");
    }

    @Test
    public void connectionEstablishedTest(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldFindHotelConventionOrFlightConventionByIdentifier() {
        // given
        String identifier = "test";
        String identifier2 = "test1";

        // when
        Optional<Trip> trip = repository.findByHotelConventionIdentifierOrFlightConventionIdentifier(identifier,identifier2);

        // then
        assertThat(trip).isPresent();
    }

    @Test
    void shouldNotFindHotelConventionOrFlightConventionByIdentifier() {
        // given
        String identifier = "test00";
        String identifier2 = "test100";

        // when
        Optional<Trip> trip = repository.findByHotelConventionIdentifierOrFlightConventionIdentifier(identifier,identifier2);

        // then
        assertThat(trip).isEmpty();
    }
}