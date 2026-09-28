package com.example.E_commerce_backend.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;

@Service
public class RefreshTokenService extends JwtCore {

    private final String SECRET_KEY;

    private final long EXPIRATION_MILLIS;

    public RefreshTokenService(
            @Value("${jwt.secret-refresh-token}") String SECRET_KEY,
            @Value("${jwt.expiration-refresh-millis}") long EXPIRATION_MILLIS) {
        this.SECRET_KEY = SECRET_KEY;
        this.EXPIRATION_MILLIS = EXPIRATION_MILLIS;
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        claims.put("type", "access");
        return buildToken(claims, userDetails, SECRET_KEY, EXPIRATION_MILLIS);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return buildToken(claims, userDetails, SECRET_KEY, EXPIRATION_MILLIS);
    }

    public String extractUsername(String token) {
        return extractClaim(token, token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return isTokenValid(token, SECRET_KEY, userDetails);
    }
}
