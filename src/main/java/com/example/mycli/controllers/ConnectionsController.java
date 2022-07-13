package com.example.mycli.controllers;

import com.example.mycli.model.NotificationRequest;
import com.example.mycli.services.ConnectionsService;
import com.example.mycli.services.EmitterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/connections")
//@SecurityRequirement(name = "basicauth")
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor

public class ConnectionsController {
    private final ConnectionsService connectionsService;
    @PostMapping("/match_from_mentee")
    public ResponseEntity<String> matchFromMentee(@RequestParam Long matchID, HttpServletRequest httpServletRequest) {
        connectionsService.matchFromMentee(matchID, httpServletRequest);
        return ResponseEntity.ok("Connection was made");
    }
    @PostMapping("/match_from_mentor")
    public ResponseEntity<String> matchFromMentor(@RequestParam Long matchID, HttpServletRequest httpServletRequest) {
        connectionsService.matchFromMentor(matchID, httpServletRequest);
        return ResponseEntity.ok("Connection was made");
    }
    @PostMapping("/break_match")
    public ResponseEntity<String> breakMatch(@RequestParam Long matchID, HttpServletRequest httpServletRequest) {
        connectionsService.breakMatch(matchID, httpServletRequest);
        return ResponseEntity.ok("Match was broken");
    }
    @GetMapping("/show_connections_mentor")
    public List<Long> getAllConnectionsStatusOne(HttpServletRequest httpServletRequest) {
        return connectionsService.getConnectionsStatusOne(httpServletRequest);
    }
    @GetMapping("/show_connections_both")
    public List<Long> getAllConnectionsStatusTwo(HttpServletRequest httpServletRequest) {
        return connectionsService.getConnectionsStatusTwo(httpServletRequest);
    }





}
