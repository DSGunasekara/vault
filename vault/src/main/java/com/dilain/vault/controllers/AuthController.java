package com.dilain.vault.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dilain.vault.dtos.auth.LoginRequest;
import com.dilain.vault.dtos.auth.RegisterRequest;
import com.dilain.vault.dtos.user.UserDto;
import com.dilain.vault.entities.User;
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
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest request) {
        try {
            authService.authenticateUser(request.username(), request.password());
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse<String>(ResponseStatus.ERROR, e.getMessage(), null));
        }
 
        Optional<User> user = userService.findByUsername(request.username());
        if (user.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<String>(ResponseStatus.ERROR, "Username is not found", null));
        }
        String secret = authService.generateToken(user.get().getUsername());
        return ResponseEntity.ok(
            new ApiResponse<String>(ResponseStatus.SUCCESS, "Login Successful", secret));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(ResponseStatus.ERROR, "Username already taken", null));
        }

        User user = userService.createUser(request);

        return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "User registered successfully", UserDto.from(user)));
    }
}