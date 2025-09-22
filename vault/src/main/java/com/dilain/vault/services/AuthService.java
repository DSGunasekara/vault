package com.dilain.vault.services;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dilain.vault.config.JwtUtils;
import com.dilain.vault.dtos.auth.LoginRequest;
import com.dilain.vault.dtos.auth.LoginResponse;
import com.dilain.vault.dtos.auth.RefreshRequest;
import com.dilain.vault.entities.RefreshToken;
import com.dilain.vault.entities.User;
import com.dilain.vault.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse login(LoginRequest request) {
        User user = userService.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        authenticateUser(user.getUsername(), request.password());

        String accessToken = jwtUtil.generateToken(user.getUsername());
        String refreshToken = refreshTokenService.create(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refresh(RefreshRequest token) {
        RefreshToken foundToken = refreshTokenService.findByToken(token.refreshToken())
                .orElseThrow(() -> new NotFoundException("Refresh token not found"));

        if (foundToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenService.delete(foundToken.getId());
            throw new RuntimeException("Refresh token expired");
        }

        String accessToken = jwtUtil.generateToken(foundToken.getUser().getUsername());
        String refreshToken = refreshTokenService.create(foundToken.getUser());

        return new LoginResponse(accessToken, refreshToken);

    }

    private Authentication authenticateUser(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
    }
}
