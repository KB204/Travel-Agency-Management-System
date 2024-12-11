package ma.emsi.volservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.emsi.volservice.dto.airline.AirlineResponseDTO;
import ma.emsi.volservice.dto.convention.ConventionRequest;
import ma.emsi.volservice.dto.convention.ConventionResponse;
import ma.emsi.volservice.dto.flight.FlightResponse;
import ma.emsi.volservice.enums.FlightType;
import ma.emsi.volservice.service.ConventionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@WebMvcTest(ConventionController.class)
class ConventionControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ConventionService service;

    @Test
    void shouldGetFlightConventionDetails() throws Exception {
        // given
        FlightResponse flightResponse = new FlightResponse("test", "rabat", "berlin", FlightType.BUSINESS, LocalDateTime.now(),LocalDateTime.now(), new AirlineResponseDTO("test","test"));
        ConventionResponse response = new ConventionResponse(10,flightResponse);

        // when
        when(service.getConventionDetails("test")).thenReturn(response);

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/flightsConventions/{flightNo}/conventionDetails",response.flight().flightNo()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void shouldSaveNewFlightConvention() throws Exception {
        // given
        ConventionRequest request = new ConventionRequest("Test",10);

        // when
        doNothing().when(service).createNewConvention(request);

        // then
        mvc.perform(MockMvcRequestBuilders.post("/api/flightsConventions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("La convention identifié par Test a été créé avec succès"));

    }
}