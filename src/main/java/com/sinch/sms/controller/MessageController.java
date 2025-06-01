package com.sinch.sms.controller;

import com.sinch.sms.model.dto.MessageRequestDto;
import com.sinch.sms.model.vo.MessageResponseVo;
import com.sinch.sms.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageResponseVo> sendMessage(@RequestBody MessageRequestDto request) {
        MessageResponseVo response = messageService.sendMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseVo> getMessageStatus(@PathVariable String id) {
        return ResponseEntity.ok(messageService.getMessageStatus(id));
    }

    @PostMapping("/optout/{phoneNumber}")
    public ResponseEntity<String> optOut(@PathVariable String phoneNumber) {
        messageService.optOut(phoneNumber);
        return ResponseEntity.ok(String.format("Phone number %s has been opted out.", phoneNumber));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllOther(Exception e) {
        return ResponseEntity.internalServerError().body("Internal Server Error.");
    }
}
