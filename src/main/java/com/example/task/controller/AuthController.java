package com.example.task.controller;

import com.example.task.dto.auth.LoginDto;
import com.example.task.dto.auth.RegistrationDto;
import com.example.task.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authorization Controller", description = "This controller for authorization")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Method for registration", description = "This method used to create a user")
    @PostMapping("/registration")
    private ResponseEntity<?> registration(@Valid @RequestBody RegistrationDto dto) {
        log.info("Registration : email {}, name {}", dto.getEmail(), dto.getFirstname());
        return authService.registration(dto);
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String code) {
        log.info("Registration : email={}, name={}", email, code);
        return authService.verifyEmail(email, code);
    }

    @Operation(summary = "Method for authorization", description = "This method used for Login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        log.info(" Login :  email {} ", dto.getEmail());
        return authService.login(dto);
    }

}
