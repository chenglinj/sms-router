package com.sinch.sms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.sms.model.constant.MessageFormat;
import com.sinch.sms.model.constant.MessageStatus;
import com.sinch.sms.model.dto.MessageRequestDto;
import com.sinch.sms.model.vo.MessageResponseVo;
import com.sinch.sms.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageServiceImpl messageService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSendMessageReturnsOk() throws Exception {
        MessageRequestDto request = new MessageRequestDto("+61400000000", "Hello", MessageFormat.SMS.getCode());
        MessageResponseVo response = new MessageResponseVo("abc-123", MessageStatus.PENDING, "Telstra");

        when(messageService.sendMessage(any())).thenReturn(response);

        mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc-123"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.carrier").value("Telstra"));
    }

    @Test
    void testGetMessageStatusReturnsOk() throws Exception {
        MessageResponseVo response = new MessageResponseVo("abc-123", MessageStatus.DELIVERED, "Telstra");
        when(messageService.getMessageStatus("abc-123")).thenReturn(response);

        mockMvc.perform(get("/messages/abc-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"))
                .andExpect(jsonPath("$.carrier").value("Telstra"));
    }

    @Test
    void testOptOutReturnsConfirmation() throws Exception {
        mockMvc.perform(post("/messages/optout/+61400000000"))
                .andExpect(status().isOk())
                .andExpect(content().string("Phone number +61400000000 has been opted out."));

        verify(messageService, times(1)).optOut("+61400000000");
    }

    @Test
    void testSendMessageValidationFailure() throws Exception {
        MessageRequestDto invalidRequest = new MessageRequestDto("61400000000", "Hello", MessageFormat.SMS.name());

        when(messageService.sendMessage(any())).thenThrow(new IllegalArgumentException("Phone number must start with '+'"));

        mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Phone number must start with '+'"));
    }
}
