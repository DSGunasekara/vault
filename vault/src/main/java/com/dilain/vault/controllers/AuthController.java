package com.dilain.vault.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dilain.vault.dtos.auth.LoginRequest;
import com.dilain.vault.dtos.auth.LoginResponse;
import com.dilain.vault.dtos.auth.RefreshRequest;
import com.dilain.vault.dtos.auth.RegisterRequest;
import com.dilain.vault.dtos.user.UserDto;
import com.dilain.vault.enums.ResponseStatus;
import com.dilain.vault.services.AuthService;
import com.dilain.vault.services.UserService;
import com.dilain.vault.utils.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse tokens = authService.login(request);
        return ResponseEntity.ok(
                new ApiResponse<>(ResponseStatus.SUCCESS, "Login Successful", tokens));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest request) {
        UserDto user = userService.createUser(request);

        return ResponseEntity
                .ok(new ApiResponse<>(ResponseStatus.SUCCESS, "User registered successfully", user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@Valid @RequestBody RefreshRequest request) {
        LoginResponse tokens = authService.refresh(request);

        return ResponseEntity
                .ok(new ApiResponse<>(ResponseStatus.SUCCESS, "Token refreshed successfully.", tokens));
    }
}