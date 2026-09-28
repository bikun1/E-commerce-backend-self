package com.example.E_commerce_backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.E_commerce_backend.security.AccessTokenService;
import com.example.E_commerce_backend.security.CustomUserDetails;
import com.example.E_commerce_backend.security.RefreshTokenService;

import dto.request.LoginRequest;
import dto.response.JwtResponseBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

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
}
