package com.example.E_commerce_backend.controller;

import org.springframework.http.HttpHeaders;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.E_commerce_backend.dto.request.LoginRequest;
import com.example.E_commerce_backend.dto.response.ApiResult;
import com.example.E_commerce_backend.dto.response.JwtResponseBody;
import com.example.E_commerce_backend.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

    private final AuthService authService;

    private final long REFRESH_EXPIRATION_MILLS;

    public AuthController(AuthService authService,
            @Value("${jwt.expiration-refresh-millis}") long refresh_expiration_mills) {
        this.authService = authService;
        this.REFRESH_EXPIRATION_MILLS = refresh_expiration_mills;
    }

    @PostMapping("login")
    @Operation(operationId = "login", summary = "Login", description = "Authenticate user and return JWT access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Wrong username or password", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class)))
    })
    public ResponseEntity<ApiResult<JwtResponseBody>> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponseBody responseBody = authService.login(loginRequest);

        ResponseCookie cookie = ResponseCookie.from("Refresh-token", responseBody.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Striect")
                .path("/api/auth/refresh")
                .maxAge(Duration.ofMillis(REFRESH_EXPIRATION_MILLS))
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResult.success("Login successful", responseBody));
    }
}