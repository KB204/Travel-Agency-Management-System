package net.travelsystem.reservationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.enums.ReservationStatus;
import net.travelsystem.reservationservice.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
    @MockBean
    private ReservationService service;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    List<ReservationResponse> reservations = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.reservations = List.of(
                ReservationResponse.builder()
                        .identifier("test")
                        .reservationDate(LocalDateTime.now())
                        .status(ReservationStatus.PENDING)
                        .build(),
                ReservationResponse.builder()
                    .identifier("test2")
                    .reservationDate(LocalDateTime.now())
                    .status(ReservationStatus.PENDING)
                    .build()
        );
    }

    /*@Test
    void shouldFindAllReservations() throws Exception {
        // when
        when(service.getAllReservations()).thenReturn(reservations);

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/reservations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(reservations)));
    }*/

    @Test
    void shouldFindReservationDetails() throws Exception {
        // given
        String identifier = "test";

        // when
        when(service.reservationTotalAmount(identifier)).thenReturn(reservations.getFirst().totalPrice());

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/reservations/{identifier}/reservation/totalPrice",identifier))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}