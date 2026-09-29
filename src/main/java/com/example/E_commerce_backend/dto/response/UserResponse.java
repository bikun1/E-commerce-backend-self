package com.example.E_commerce_backend.dto.response;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.E_commerce_backend.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "user response (data of ApiResult)")
public record UserResponse(
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long id,

        @Schema(example = "Admin", requiredMode = Schema.RequiredMode.REQUIRED) String username,

        @Schema(example = "user@test.com", requiredMode = Schema.RequiredMode.REQUIRED) String email,

        @Schema(example = "[\"User Role\"]", requiredMode = Schema.RequiredMode.REQUIRED) Set<String> roles) {

    public static UserResponse fromEntity(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles);
    }
}