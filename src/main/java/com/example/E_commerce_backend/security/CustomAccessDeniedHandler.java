package com.example.E_commerce_backend.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import dto.response.ApiResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ApiResult<Void> apiResult = ApiResult.error(
                "Access denied: you don't have permission to access this resource",
                ApiResult.ErrorCode.FORBIDDEN.name());

        objectMapper.writeValue(response.getWriter(), apiResult);
    }

}
