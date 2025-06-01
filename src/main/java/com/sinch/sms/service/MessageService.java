package com.sinch.sms.service;

import com.sinch.sms.model.dto.MessageRequestDto;
import com.sinch.sms.model.vo.MessageResponseVo;

public interface MessageService {

    MessageResponseVo sendMessage(MessageRequestDto request);

    MessageResponseVo getMessageStatus(String id);

    void optOut(String phoneNumber);
}
