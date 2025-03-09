package net.travelsystem.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.travelsystem.paymentservice.dto.refund.RefundRequest;
import net.travelsystem.paymentservice.service.RefundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(value = RefundController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
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
        doNothing().when(service).saveNewRefund(eq(paymentIdentifier));

        // then
        mvc.perform(MockMvcRequestBuilders.post("/api/refunds/{identifier}/newRefund",paymentIdentifier))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("La demande de remboursement a été crée avec succès"));
    }

    @Test
    void shouldDeclineRefund() throws Exception {
        // given
        String paymentIdentifier = "test";

        // when
        doNothing().when(service).declineRefund(eq(paymentIdentifier));

        // then
        mvc.perform(MockMvcRequestBuilders.put("/api/refunds/{identifier}/decline",paymentIdentifier))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("Le remboursement identifié par test a été rejeté"));
    }

    @Test
    void shouldAcceptRefund() throws Exception {
        // given
        String paymentIdentifier = "test";
        RefundRequest request = new RefundRequest(5000);

        // when
        doNothing().when(service).acceptRefund(eq(paymentIdentifier),any());

        // then
        mvc.perform(MockMvcRequestBuilders.put("/api/refunds/{identifier}/accept",paymentIdentifier)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("Le remboursement identifié par test a été accepté"));
    }
}