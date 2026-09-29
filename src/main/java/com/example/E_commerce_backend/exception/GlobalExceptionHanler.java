package com.example.E_commerce_backend.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.E_commerce_backend.dto.response.ApiResult;
import com.example.E_commerce_backend.dto.response.ApiResult.ErrorCode;
import com.example.E_commerce_backend.dto.response.ApiResult.FieldErrorDetail;

@RestControllerAdvice
public class GlobalExceptionHanler {

    @ExceptionHandler(ResourceDuplicationException.class)
    public ResponseEntity<ApiResult<Void>> handleGlobalException(ResourceDuplicationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(ex.getMessage(), ErrorCode.CONFLICT.name()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResult<Void>> handleGlobalException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResult.error(ex.getMessage(), ErrorCode.NOT_FOUND.name()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldErrorDetail)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.validationError(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.error(ex.getMessage(), ErrorCode.INTERNAL_ERROR.name()));
    }

    private FieldErrorDetail toFieldErrorDetail(FieldError fieldError) {
        return new FieldErrorDetail(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
