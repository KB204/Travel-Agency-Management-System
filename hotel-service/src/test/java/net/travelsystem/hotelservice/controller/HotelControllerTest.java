package net.travelsystem.hotelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.travelsystem.hotelservice.dto.hotel.HotelRequest;
import net.travelsystem.hotelservice.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@WebMvcTest(HotelController.class)
class HotelControllerTest {
    @MockBean
    private HotelService service;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldSaveNewHotel() throws Exception {
        HotelRequest request = new HotelRequest("test","test","test");

        doNothing().when(service).createNewHotel(any());
        mvc.perform(MockMvcRequestBuilders.post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Hotel test a été créé avec succès"));
    }
    @Test
    void shouldUpdateExistingHotel() throws Exception {
        long id = 1;
        HotelRequest request = new HotelRequest("test","test","test");

        doNothing().when(service).updateHotel(eq(id),any());
        mvc.perform(MockMvcRequestBuilders.put("/api/hotels/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("Hotel test a été modifié avec succès"));
    }
}