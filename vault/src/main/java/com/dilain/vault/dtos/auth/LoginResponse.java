package com.dilain.vault.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginResponse(
    @NotBlank String accessToken,
    @NotBlank String refreshToken
) {
    
}
