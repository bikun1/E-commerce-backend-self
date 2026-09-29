package com.example.E_commerce_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Register request body")
public record RegisterRequest(
        @Schema(description = "username", example = "Admin", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Username is required") String username,

        @Schema(description = "password", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Password is required") @Pattern(regexp = "^(?=.*[0-9]).{3,}$") String password) {
}
