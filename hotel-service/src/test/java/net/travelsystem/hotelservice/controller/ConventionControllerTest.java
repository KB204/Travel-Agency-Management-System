package net.travelsystem.hotelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.travelsystem.hotelservice.dto.convention.ConventionRequest;
import net.travelsystem.hotelservice.service.ConventionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@WebMvcTest(ConventionController.class)
class ConventionControllerTest {
    @MockitoBean
    private ConventionService service;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldSaveNewHotelConvention() throws Exception {
        ConventionRequest request = new ConventionRequest("test", "test", LocalDate.now(), LocalDate.of(2024,12,12));

        doNothing().when(service).createHotelConvention(any());
        mvc.perform(MockMvcRequestBuilders.post("/api/conventions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("La convention avec l'hotel test situé a test a été créé avec succès"));
    }
}