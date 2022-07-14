package com.example.mycli.controllers;

import com.example.mycli.entity.Connection;
import com.example.mycli.model.NotificationRequest;
import com.example.mycli.services.EmitterService;
import com.example.mycli.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final EmitterService emitterService;
    private final MessageService messageService;

    @PostMapping("/send/{toWhom}")
    public ResponseEntity<?> send(@PathVariable Long toWhom, @RequestBody @Valid NotificationRequest request,
                                  HttpServletRequest httpServletRequest) {
        emitterService.pushMessage(toWhom, request.getMessage(), httpServletRequest);
        messageService.saveMessage(toWhom, request.getMessage(), httpServletRequest);
        return ResponseEntity.ok().body("message pushed to user " + toWhom + " and saved");
    }

    @GetMapping()
    public Connection getAllMessages(@RequestParam Long friendID, HttpServletRequest httpServletRequest) {
        return messageService.getAllMessages(friendID, httpServletRequest);
    }
}
