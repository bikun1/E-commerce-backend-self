package com.example.E_commerce_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.E_commerce_backend.service.AuthService;

import dto.request.LoginRequest;
import dto.response.ApiResponse;
import dto.response.JwtResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    @Operation(summary = "Login", description = "Authenticate user and return JWt access token")
    public ResponseEntity<ApiResponse<JwtResponseBody>> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponseBody responseBody = authService.login(loginRequest);
        responseBody.setRefreshToken(null);

        return ResponseEntity.ok(ApiResponse.success("Login successful", responseBody));
    }
}
