package net.travelsystem.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.travelsystem.paymentservice.service.RefundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;

@WebMvcTest(RefundController.class)
class RefundControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private RefundService service;

    @Test
    void shouldCreateNewRefund() throws Exception {
        // given
        String paymentIdentifier = "test";

        // when
        doNothing().when(service).saveNewRefund(paymentIdentifier);

        // then
        mvc.perform(MockMvcRequestBuilders.post("/api/refunds/{identifier}/newRefund",paymentIdentifier))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("La demande de remboursement a été crée avec succès"));
    }

    @Test
    void declineRefund() {
    }

    @Test
    void acceptRefund() {
    }
}