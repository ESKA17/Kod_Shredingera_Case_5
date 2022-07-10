package com.example.mycli.controllers;

import com.example.mycli.entity.UserEntity;
import com.example.mycli.repository.UserEntityRepo;
import com.example.mycli.services.AccountAuthenticationService;
import com.example.mycli.services.AccountRegistrationService;
import com.example.mycli.model.AuthRequest;
import com.example.mycli.model.RegRequest;
//import com.example.mycli.services.UserStatusService;
import com.example.mycli.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Log
public class AuthController {
    private final UserService userService;
    private final AccountRegistrationService accountRegistrationService;
    private final AccountAuthenticationService accountAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegRequest registrationRequest) {
        String email = registrationRequest.getEmail();
        String password  = registrationRequest.getPassword();
        int role = registrationRequest.getRole();
        String fullName = registrationRequest.getFullName();
        accountRegistrationService.registerAccount(email, password, fullName, role);
        return ResponseEntity.ok("Account was successfully registered");
    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody @Valid AuthRequest authRequest,
                                       HttpServletResponse httpServletResponse) {
        String email = authRequest.getEmail();
        String password = authRequest.getPassword();
        accountAuthenticationService.authenticateAccount(email, password, httpServletResponse);
        return ResponseEntity.ok("Successfully authenticated");
    }
    @GetMapping("/users")
    public List<UserEntity> users() {
        return userService.findAllUsers();
    }
    @PostMapping("/logout")
    public void logout() {
        log.info("logout");
    }

    @PostMapping("/process_register")
    public ResponseEntity<String> processRegister(String email, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
        accountRegistrationService.twoStepVerificationEmail(email, getSiteURL(request));
        return ResponseEntity.ok("Registration verification email was sent");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@Param("code") String code) {
        if (accountRegistrationService.verifyUser(code)) {
            return ResponseEntity.ok("Successful verification");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already active");
        }
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

}

