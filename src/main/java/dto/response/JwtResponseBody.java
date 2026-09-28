package dto.response;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.E_commerce_backend.security.CustomUserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class JwtResponseBody {
        private String accessToken;
        private String refreshToken;
        private Long id;
        private String username;
        private String email;
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