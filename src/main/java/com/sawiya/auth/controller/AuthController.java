package com.sawiya.auth.controller;

import com.sawiya.auth.dto.SigninRequestDTO;
import com.sawiya.auth.dto.SigninResponseDTO;
import com.sawiya.auth.dto.SignupRequestDTO;
import com.sawiya.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup( @Valid @RequestBody SignupRequestDTO request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponseDTO> signin(@Valid @RequestBody SigninRequestDTO request) {
        SigninResponseDTO response = authService.signin(request);
        return ResponseEntity.ok(response);
    }
}
