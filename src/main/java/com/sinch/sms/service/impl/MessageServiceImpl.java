package com.sinch.sms.service.impl;

import com.sinch.sms.model.constant.Carriers;
import com.sinch.sms.model.constant.MessageFormat;
import com.sinch.sms.model.constant.MessageStatus;
import com.sinch.sms.model.constant.SmsConstants;
import com.sinch.sms.model.dto.MessageRequestDto;
import com.sinch.sms.model.entity.Message;
import com.sinch.sms.model.vo.MessageResponseVo;
import com.sinch.sms.repository.MessageRepository;
import com.sinch.sms.repository.OptOutRepository;
import com.sinch.sms.service.MessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Duration STATUS_DELAY = Duration.ofSeconds(10);

    private final AtomicInteger auCounter = new AtomicInteger(0);
    private final MessageRepository messageRepository;
    private final OptOutRepository optOutRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, OptOutRepository optOutRepository) {
        this.messageRepository = messageRepository;
        this.optOutRepository = optOutRepository;
    }

    @Override
    public MessageResponseVo sendMessage(MessageRequestDto request) {
        validatePhoneNumber(request.getDestinationNumber());
        validateFormat(request.getFormat());
        validateContent(request.getContent());

        String id = UUID.randomUUID().toString();
        String carrier = determineCarrier(request.getDestinationNumber());
        MessageStatus status = MessageStatus.PENDING;
        LocalDateTime now = LocalDateTime.now();

        Message message = new Message(id, request.getDestinationNumber(), request.getContent(), carrier, status, now, now);
        messageRepository.save(message);

        MessageResponseVo messageVo = new MessageResponseVo();
        BeanUtils.copyProperties(message, messageVo);
        return messageVo;
    }

    @Override
    public MessageResponseVo getMessageStatus(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Message ID should not be blank.");
        }
        Message message = messageRepository.findById(id);
        if (message == null) {
            throw new IllegalArgumentException("Message ID not found: " + id);
        }

        MessageResponseVo messageVo = new MessageResponseVo();
        BeanUtils.copyProperties(message, messageVo);
        return messageVo;
    }

    @Override
    public void optOut(String phoneNumber) {
        validatePhoneNumber(phoneNumber);

        if (optOutRepository.exists(phoneNumber)) {
            throw new IllegalArgumentException(String.format("Phone number %s has already been opted out.", phoneNumber));
        }

        optOutRepository.save(phoneNumber);
    }

    // Determine carrier based on routing rules
    private String determineCarrier(String phoneNumber) {
        if (phoneNumber.startsWith(SmsConstants.AU_COUNTRY_CODE)) {
            // AU (+61): "Telstra" or "Optus" (alternate)
            return auCounter.getAndIncrement() % 2 == 0 ? Carriers.TELSTRA.getCode() : Carriers.OPTUS.getCode();
        } else if (phoneNumber.startsWith(SmsConstants.NZ_COUNTRY_CODE)) {
            // NZ (+64): "Spark"
            return Carriers.SPARK.getCode();
        } else {
            // Others
            return Carriers.GLOBAL.getCode();
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber) || !phoneNumber.startsWith("+")) {
            throw new IllegalArgumentException("Phone number must start with '+' and country code.");
        }

        if (phoneNumber.startsWith(SmsConstants.AU_COUNTRY_CODE)) {
            // Australia: +61 followed by 9 digits
            if (!phoneNumber.matches(SmsConstants.AU_NUMBER_REGEX)) {
                throw new IllegalArgumentException("Invalid Australian phone number format. Should be +61 followed by 9 digits.");
            }
        } else if (phoneNumber.startsWith(SmsConstants.NZ_COUNTRY_CODE)) {
            // New Zealand: +64 followed by 8 or 9 digits
            if (!phoneNumber.matches(SmsConstants.NZ_NUMBER_REGEX)) {
                throw new IllegalArgumentException("Invalid New Zealand phone number format. Should be +64 followed by 8 or 9 digits.");
            }
        } else {
            // Global: must be at least 8 digits total
            if (!phoneNumber.matches(SmsConstants.GLOBAL_NUMBER_REGEX)) {
                throw new IllegalArgumentException("Invalid global phone number format. Should start with '+' and be at least 8 digits.");
            }
        }
    }

    private void validateFormat(String format) {
        Optional<MessageFormat> messageFormat = MessageFormat.fromCode(format);
        if (messageFormat.isEmpty()) {
            throw new IllegalArgumentException(String.format("Unsupported message format: %s.", format));
        }
    }

    private void validateContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("SMS content should not be blank.");
        }

        if (content.length() > SmsConstants.MAX_SMS_LENGTH) {
            throw new IllegalArgumentException("SMS content exceeds maximum allowed length of 160 characters.");
        }
        // Considering adding other validations e.g. sensitive words...
    }

    private boolean isOptedOut(String phoneNumber) {
        return optOutRepository.exists(phoneNumber);
    }

    @Scheduled(fixedRate = 5000)
    public void updateMessageStatus() {
        LocalDateTime now = LocalDateTime.now();
        for (Message message : messageRepository.findAll()) {
            if (message.getStatus() == MessageStatus.PENDING &&
                    Duration.between(message.getUpdateDate(), now).compareTo(STATUS_DELAY) >= 0) {
                boolean isOptedOut = isOptedOut(message.getDestinationNumber());
                message.setStatus(isOptedOut ? MessageStatus.BLOCKED : MessageStatus.SENT);
                message.setUpdateDate(now);
            } else if (message.getStatus() == MessageStatus.SENT &&
                    Duration.between(message.getUpdateDate(), now).compareTo(STATUS_DELAY) >= 0) {
                message.setStatus(MessageStatus.DELIVERED);
                message.setUpdateDate(now);
            }
        }
    }
}
