package net.travelsystem.hotelservice.dao;

import net.travelsystem.hotelservice.entities.Hotel;
import net.travelsystem.hotelservice.entities.Room;
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
class HotelRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");
    @Autowired
    HotelRepository repository;

    List<Hotel> hotels = new ArrayList<>();
    List<Room> rooms = new ArrayList<>();

    @BeforeEach
    void setUp(){
        System.out.println("------------------------------------------------------------");
        this.hotels = List.of(
                new Hotel(null,"test","test","test",rooms),
                new Hotel(null,"test2","test2","test2",rooms)
        );
        repository.saveAll(hotels);
        System.out.println("--------------------------------------------------------------");
    }
    @Test
    public void connectionEstablishedTest(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldFindHotelByNameAndLocationIgnoringTheCase() {
        // given
        String hotelName = "test";
        String hotelLocation = "test";

        // when
        Optional<Hotel> hotel = repository.findByNameIgnoreCaseAndLocationIgnoreCase(hotelName,hotelLocation);

        // then
        assertThat(hotel).isPresent();
    }
    @Test
    void shouldNotFindHotelByNameAndLocation() {
        // given
        String hotelName = "test00";
        String hotelLocation = "test000";

        // when
        Optional<Hotel> hotel = repository.findByNameIgnoreCaseAndLocationIgnoreCase(hotelName,hotelLocation);

        // then
        assertThat(hotel).isEmpty();
    }
}