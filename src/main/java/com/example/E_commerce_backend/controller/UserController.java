package com.example.E_commerce_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.E_commerce_backend.dto.request.UpdateUserRequest;
import com.example.E_commerce_backend.dto.response.ApiResult;
import com.example.E_commerce_backend.dto.response.UserResponse;
import com.example.E_commerce_backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User management APIs")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(operationId = "getAllUsers", summary = "Get all users", description = "Retrieve all users in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class)))
    })
    public ResponseEntity<ApiResult<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResult.success("Get all users successful", userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getUserById", summary = "Get user by id", description = "Retrieve a user by their unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class)))
    })
    public ResponseEntity<ApiResult<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success("Get user successful", userService.getUserById(id)));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateUser", summary = "Update user", description = "Update the email or password of an existing user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class)))
    })
    public ResponseEntity<ApiResult<UserResponse>> updateUser(@PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(ApiResult.success("Update user successful", userService.updateUser(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteUser", summary = "Delete user", description = "Delete an existing user by their unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class)))
    })
    public ResponseEntity<ApiResult<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResult.success("Delete user successful", null));
    }
}
