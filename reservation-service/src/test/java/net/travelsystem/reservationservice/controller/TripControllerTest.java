package net.travelsystem.reservationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.travelsystem.reservationservice.dto.trip.TripRequest;
import net.travelsystem.reservationservice.dto.trip.TripUpdateRequest;
import net.travelsystem.reservationservice.service.TripService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@WebMvcTest(TripController.class)
class TripControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private TripService service;


    @Test
    void shouldSaveNewTrip() throws Exception {
        // given
        TripRequest request = new TripRequest("test","test2","trip1","desc",5000.0,"rabat",
                "dubai",5,50);

        // when
        doNothing().when(service).createTrip(any());

        // then
        mvc.perform(MockMvcRequestBuilders.post("/api/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Voyage a été crée avec succès"));
    }

    @Test
    void shouldUpdateExistingTrip() throws Exception {
        // given
        long id = 1;
        TripUpdateRequest request = new TripUpdateRequest("trip2","desc2",62000.0,"casa",
                "paris",20,200);

        // when
        doNothing().when(service).updateTrip(eq(id),any());

        // then
        mvc.perform(MockMvcRequestBuilders.put("/api/trips/{id}/updateTrip",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("Voyage a été modifié avec succès"));
    }
}