package com.example.E_commerce_backend.security;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtCore {
    private SecretKey getSigningKey(String secret) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String buildToken(Map<String, Object> claims, UserDetails userDetails, String secret, long expiration) {
        String username = userDetails.getUsername();
        String jti = UUID.randomUUID().toString();
        return Jwts
                .builder()
                .id(jti)
                .subject(username)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(secret))
                .compact();
    }

    public Claims extractAllClaims(String token, String secret) {
        return Jwts.parser()
                .verifyWith(getSigningKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, String secret, Function<Claims, T> function) {
        Claims claims = extractAllClaims(token, secret);
        return function.apply(claims);
    }

    public boolean isTokenExpired(String token, String secret) {
        return extractClaim(token, secret, Claims::getExpiration).after(new Date());
    }

    public boolean isTokenValid(String token, String secret, UserDetails userDetails) {
        String username = userDetails.getUsername();
        return username != null && username.equals(extractClaim(token, secret, Claims::getSubject))
                && !isTokenExpired(token, secret);
    }
}
