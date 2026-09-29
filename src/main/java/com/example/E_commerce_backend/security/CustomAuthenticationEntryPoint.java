package com.example.E_commerce_backend.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.E_commerce_backend.dto.response.ApiResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResult<Void> apiResult = ApiResult.error(
                "Authentication required: please log in to access this resource",
                ApiResult.ErrorCode.UNAUTHORIZED.name());

        objectMapper.writeValue(response.getWriter(), apiResult);
    }

}
