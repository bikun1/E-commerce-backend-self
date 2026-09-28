package dto.response;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API response wrapper")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResult<T>(
                @Schema(description = "Request succeeded or not", example = "true", requiredMode = Schema.RequiredMode.REQUIRED) boolean success,

                @Schema(description = "Human-readable message", example = "Success", requiredMode = Schema.RequiredMode.NOT_REQUIRED) String message,

                @Schema(description = "Payload, present on success", requiredMode = Schema.RequiredMode.NOT_REQUIRED) T data,

                @Schema(description = "Error code, present on failure", example = "NOT_FOUND", allowableValues = {
                                "VALIDATION", "NOT_FOUND", "UNAUTHORIZED", "FORBIDDEN", "CONFLICT",
                                "INTERNAL_ERROR" }, requiredMode = Schema.RequiredMode.NOT_REQUIRED) String errorCode,

                @Schema(description = "Field-level validation errors", requiredMode = Schema.RequiredMode.NOT_REQUIRED) List<FieldErrorDetail> errors,

                @Schema(description = "Response time (ISO-8601)", example = "2026-09-28T12:34:56Z", requiredMode = Schema.RequiredMode.REQUIRED) Instant timestamp) {

        public static <T> ApiResult<T> success(String message, T data) {
                return new ApiResult<>(true, message, data, null, null, Instant.now());
        }

        public static <T> ApiResult<T> success(T data) {
                return success("Success", data);
        }

        public static <T> ApiResult<T> error(String message, String errorCode) {
                return new ApiResult<>(false, message, null, errorCode, null, Instant.now());
        }

        public static <T> ApiResult<T> error(String message, String errorCode, List<FieldErrorDetail> errors) {
                return new ApiResult<>(false, message, null, errorCode, errors, Instant.now());
        }

        public static <T> ApiResult<T> error(String errorCode) {
                return error("Error", errorCode);
        }

        public static <T> ApiResult<T> validationError(List<FieldErrorDetail> errors) {
                return error("Validation error", ErrorCode.VALIDATION.name(), errors);
        }

        @Schema(description = "Validation error for one field")
        public record FieldErrorDetail(
                        @Schema(example = "username", requiredMode = Schema.RequiredMode.REQUIRED) String field,
                        @Schema(example = "Username is required", requiredMode = Schema.RequiredMode.REQUIRED) String message) {
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