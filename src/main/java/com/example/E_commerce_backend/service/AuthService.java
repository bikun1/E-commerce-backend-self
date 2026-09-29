package com.example.E_commerce_backend.service;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.E_commerce_backend.dto.request.LoginRequest;
import com.example.E_commerce_backend.dto.request.RegisterRequest;
import com.example.E_commerce_backend.dto.response.JwtResponseBody;
import com.example.E_commerce_backend.dto.response.UserResponse;
import com.example.E_commerce_backend.entity.Role;
import com.example.E_commerce_backend.entity.User;
import com.example.E_commerce_backend.exception.ResourceDuplicationException;
import com.example.E_commerce_backend.exception.ResourceNotFoundException;
import com.example.E_commerce_backend.repository.RoleRepository;
import com.example.E_commerce_backend.repository.UserRepository;
import com.example.E_commerce_backend.security.AccessTokenService;
import com.example.E_commerce_backend.security.CustomUserDetails;
import com.example.E_commerce_backend.security.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public JwtResponseBody login(LoginRequest loginRequest) {
        // rate limit

        // authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        // set securityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // generate jwt token (access token + refresh token)
        String accessToken = accessTokenService.generateToken(userDetails);
        String refreshToken = refreshTokenService.generateToken(userDetails);

        // return JwtResponseBody
        return JwtResponseBody.of(accessToken, refreshToken, (CustomUserDetails) userDetails);
    }

    public UserResponse register(RegisterRequest registerRequest) {
        // check username in DB
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new ResourceDuplicationException("User already exists with username = " + registerRequest.username());
        }

        // gen new User and set password by bcrypt
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        // fetch role from DB
        Role norRole = roleRepository.findByName("User Role").orElseThrow(
                () -> new ResourceNotFoundException("Normal user role not exists"));

        user.setRoles(Set.of(norRole));

        userRepository.save(user);

        // map user to UserResponse
        return UserResponse.fromEntity(user);
    }
}
