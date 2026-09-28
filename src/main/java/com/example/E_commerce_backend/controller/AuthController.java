package com.example.E_commerce_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.E_commerce_backend.service.AuthService;

import dto.request.LoginRequest;
import dto.response.ApiResult;
import dto.response.JwtResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(operationId = "login", summary = "Login", description = "Authenticate user and return JWT access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Wrong username or password", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class)))
    })
    public ResponseEntity<ApiResult<JwtResponseBody>> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponseBody responseBody = authService.login(loginRequest);
        responseBody.setRefreshToken(null);

        return ResponseEntity.ok(ApiResult.success("Login successful", responseBody));
    }
}