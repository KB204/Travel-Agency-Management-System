package net.travelsystem.hotelservice.dao;

import net.travelsystem.hotelservice.entities.Convention;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConventionRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    ConventionRepository repository;

    List<Convention> conventions = new ArrayList<>();

    @BeforeEach
    void setUp(){
        System.out.println("------------------------------------------------------------");
        this.conventions = List.of(
                Convention.builder().identifier("test")
                        .checkInDate(LocalDate.now())
                        .checkOutDate(LocalDate.now())
                        .availableRooms(10)
                        .build(),
                Convention.builder().identifier("test2")
                        .checkInDate(LocalDate.now())
                        .checkOutDate(LocalDate.now())
                        .availableRooms(15)
                        .build()
        );
        repository.saveAll(conventions);
        System.out.println("--------------------------------------------------------------");
    }
    @Test
    public void connectionEstablishedTest(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldFindConventionByIdentifierIgnoreCase() {
        // given
        String identifier = "test";

        // when
        Optional<Convention> convention = repository.findByIdentifierIgnoreCase(identifier);

        // then
        assertThat(convention).isPresent();
    }

    @Test
    void shouldNotFindConventionByIdentifier() {
        // given
        String identifier = "ss";

        // when
        Optional<Convention> convention = repository.findByIdentifierIgnoreCase(identifier);

        // then
        assertThat(convention).isEmpty();
    }
}