package com.sinch.sms.controller;

import com.sinch.sms.service.OptOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/optout")
public class OptOutController {

    private final OptOutService optOutService;

    @Autowired
    public OptOutController(OptOutService optOutService) {
        this.optOutService = optOutService;
    }

    @PostMapping("/{phoneNumber}")
    public ResponseEntity<String> optOut(@PathVariable String phoneNumber) {
        optOutService.optOut(phoneNumber);
        return ResponseEntity.ok(String.format("Phone number %s has been opted out.", phoneNumber));
    }

    @DeleteMapping("/{phoneNumber}")
    public ResponseEntity<String> cancelOptOut(@PathVariable String phoneNumber) {
        optOutService.cancelOptOut(phoneNumber);
        return ResponseEntity.ok("Phone number " + phoneNumber + " has been removed from opt-out list.");
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