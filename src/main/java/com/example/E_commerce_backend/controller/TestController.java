package com.example.E_commerce_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Test", description = "Simple test endpoints")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class))),
        @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResult.class)))
})
@SecurityRequirement(name = "bearerAuth")
public class TestController {

    @GetMapping("/test1")
    @Operation(operationId = "test1", summary = "Test endpoint 1")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain"))
    public String test1() {
        return "test1";
    }

    @GetMapping("/test2")
    @Operation(operationId = "test2", summary = "Test endpoint 2")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain"))
    public String test2() {
        return "test2";
    }
}