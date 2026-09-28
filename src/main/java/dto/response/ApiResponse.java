package dto.response;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
                boolean success,
                String message,
                T data,
                String errorCode,
                List<FieldErrorDetail> errors,
                Instant timestamp) {

        public static <T> ApiResponse<T> success(String message, T data) {
                return new ApiResponse<>(true, message, data, null, null, Instant.now());
        }

        public static <T> ApiResponse<T> success(T data) {
                return success("Success", data);
        }

        public static <T> ApiResponse<T> error(String message, String errorCode) {
                return new ApiResponse<>(false, message, null, errorCode, null, Instant.now());
        }

        public static <T> ApiResponse<T> error(String message, String errorCode, List<FieldErrorDetail> errors) {
                return new ApiResponse<>(false, message, null, errorCode, errors, Instant.now());
        }

        public static <T> ApiResponse<T> error(String errorCode) {
                return error("Error", errorCode);
        }

        public static <T> ApiResponse<T> validationError(List<FieldErrorDetail> errors) {
                return error("Validation error", ErrorCode.VALIDATION.name(), errors);
        }

        public record FieldErrorDetail(
                        String field,
                        String message) {
        }

        public enum ErrorCode {
                VALIDATION,
                NOT_FOUND,
                UNAUTHORIZED,
                FORBIDDEN,
                CONFLICT,
                INTERNAL_ERROR
        }
}