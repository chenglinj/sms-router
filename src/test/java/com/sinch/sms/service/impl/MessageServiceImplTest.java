package com.sinch.sms.service.impl;

import com.sinch.sms.model.constant.MessageFormat;
import com.sinch.sms.model.constant.MessageStatus;
import com.sinch.sms.model.dto.MessageRequestDto;
import com.sinch.sms.model.entity.Message;
import com.sinch.sms.model.vo.MessageResponseVo;
import com.sinch.sms.repository.impl.MessageInMemoryRepository;
import com.sinch.sms.repository.impl.OptOutInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceImplTest {

    private MessageServiceImpl messageService;
    private MessageInMemoryRepository messageInMemoryRepository;

    @BeforeEach
    void setup() {
        MessageInMemoryRepository messageRepository = new MessageInMemoryRepository();
        OptOutInMemoryRepository optOutRepository = new OptOutInMemoryRepository();
        messageService = new MessageServiceImpl(messageRepository, optOutRepository);
        messageInMemoryRepository = messageRepository;
    }

    @Test
    void testSendToAustralianNumber() {
        MessageRequestDto request = new MessageRequestDto("+61400000000", "Hello world", MessageFormat.SMS.getCode());
        MessageResponseVo response = messageService.sendMessage(request);

        assertNotNull(response.getId());
        assertEquals(MessageStatus.PENDING, response.getStatus());
        assertTrue(response.getCarrier().equals("Telstra") || response.getCarrier().equals("Optus"));
    }

    @Test
    void testSendToNewZealandNumber() {
        MessageRequestDto request = new MessageRequestDto("+6421000000", "World hello", MessageFormat.SMS.getCode());
        MessageResponseVo response = messageService.sendMessage(request);

        assertNotNull(response.getId());
        assertEquals("Spark", response.getCarrier());
    }

    @Test
    void testSendToOptedOutNumberIsBlocked() {
        String phoneNumber = "+61400000001";
        messageService.optOut(phoneNumber);

        MessageRequestDto request = new MessageRequestDto(phoneNumber, "Blocked test", MessageFormat.SMS.getCode());
        MessageResponseVo response = messageService.sendMessage(request);

        // Manually simulate passage of time and run status update
        Message message = messageInMemoryRepository.findById(response.getId());
        message.setUpdateDate(LocalDateTime.now().minusSeconds(10));
        messageService.updateMessageStatus();

        Message updated = messageInMemoryRepository.findById(response.getId());
        assertEquals(MessageStatus.BLOCKED, updated.getStatus());
    }

    @Test
    void testInvalidFormatRejected() {
        MessageRequestDto request = new MessageRequestDto("+61400000002", "Invalid format", "EMAIL");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage(request);
        });
        assertTrue(exception.getMessage().contains("Unsupported message format"));
    }

    @Test
    void testTooLongContentRejected() {
        String longContent = "A".repeat(161);
        MessageRequestDto request = new MessageRequestDto("+61400000003", longContent, MessageFormat.SMS.name());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage(request);
        });
        assertTrue(exception.getMessage().contains("exceeds maximum allowed length"));
    }

    @Test
    void testInvalidPhoneNumberRejected() {
        MessageRequestDto request = new MessageRequestDto("61400000004", "Missing plus sign", MessageFormat.SMS.name());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage(request);
        });
        assertTrue(exception.getMessage().contains("Phone number must start with '+'"));
    }
}
