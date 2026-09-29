package com.example.E_commerce_backend.dto.response;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.E_commerce_backend.security.CustomUserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Login result")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class JwtResponseBody {
        @Schema(description = "Jwt access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
        private String accessToken;

        @Schema(description = "not return in response (refresh token be handled seperately)", accessMode = Schema.AccessMode.READ_ONLY, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private String refreshToken;

        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        private Long id;

        @Schema(example = "Admin", requiredMode = Schema.RequiredMode.REQUIRED)
        private String username;

        @Schema(example = "admin@test.com", requiredMode = Schema.RequiredMode.REQUIRED)
        private String email;

        @Schema(example = "[\"Admin Role\"]", requiredMode = Schema.RequiredMode.REQUIRED)
        private Set<String> roles;

        private JwtResponseBody(String accessToken, String refreshToken, Long id, String username, String email,
                        Set<String> roles) {
                this.accessToken = accessToken;
                this.refreshToken = refreshToken;
                this.id = id;
                this.username = username;
                this.email = email;
                this.roles = roles;
        }

        public static JwtResponseBody of(String access, String refresh, CustomUserDetails userDetails) {
                Set<String> roles = userDetails.getRoles().stream()
                                .map(r -> r.getName())
                                .collect(Collectors.toSet());

                return new JwtResponseBody(access, refresh, userDetails.getId(), userDetails.getUsername(),
                                userDetails.getEmail(), roles);
        }

        public void setRefreshToken(String refreshToken) {
                this.refreshToken = refreshToken;
        }
}