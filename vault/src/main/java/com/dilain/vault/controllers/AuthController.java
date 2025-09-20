package com.dilain.vault.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dilain.vault.config.JwtUtils;
import com.dilain.vault.dtos.auth.LoginRequest;
import com.dilain.vault.dtos.auth.RegisterRequest;
import com.dilain.vault.entities.User;
import com.dilain.vault.enums.ResponseStatus;
import com.dilain.vault.repositories.UserRepository;
import com.dilain.vault.services.CustomUserDetailsService;
import com.dilain.vault.utils.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        Optional<User> user = userRepository.findByUsername(request.username());
        if (!user.isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse<String>(ResponseStatus.ERROR, "Username is not found", null));
        }
        String secret = jwtUtil.generateToken(user.get().getUsername());
        return ResponseEntity.ok(
            new ApiResponse<String>(ResponseStatus.SUCCESS, "Login Successful", secret));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse<String>(ResponseStatus.ERROR, "Username already taken", null));
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse<String>(ResponseStatus.SUCCESS, "User registered successfully", null));
    }
}