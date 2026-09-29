package com.example.E_commerce_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.E_commerce_backend.dto.response.ApiResult;
import com.example.E_commerce_backend.dto.response.ApiResult.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHanler {

    @ExceptionHandler(ResourceDuplicationException.class)
    public ResponseEntity<ApiResult<Void>> handleGlobalException(ResourceDuplicationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(ex.getMessage(), ErrorCode.CONFLICT.name()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResult<Void>> handleGlobalException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(ex.getMessage(), ErrorCode.NOT_FOUND.name()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.error(ex.getMessage(), ErrorCode.INTERNAL_ERROR.name()));
    }
}
