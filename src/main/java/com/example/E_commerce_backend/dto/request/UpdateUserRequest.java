package com.example.E_commerce_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Update user request body")
public record UpdateUserRequest(
        @Schema(description = "user email", example = "user@test.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED) @Email(message = "Email must be valid") String email,

        @Schema(description = "new password", example = "123456", requiredMode = Schema.RequiredMode.NOT_REQUIRED) @Pattern(regexp = "^(?=.*[0-9]).{3,}$", message = "Password must contain at least 3 characters and at least one number") String password) {
}
