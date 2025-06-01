package com.sinch.sms.controller;

import com.sinch.sms.service.impl.OptOutServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OptOutController.class)
class OptOutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OptOutServiceImpl optOutService;

    @Test
    void testOptOutReturnsConfirmation() throws Exception {
        String phoneNumber = "+61400000000";

        mockMvc.perform(post("/optout/" + phoneNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("Phone number +61400000000 has been opted out."));

        verify(optOutService, times(1)).optOut(phoneNumber);
    }

    @Test
    void testCancelOptOut() throws Exception {
        String phoneNumber = "+61400000000";

        mockMvc.perform(delete("/optout/" + phoneNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("Phone number +61400000000 has been removed from opt-out list."));

        verify(optOutService, times(1)).cancelOptOut(phoneNumber);
    }
}
