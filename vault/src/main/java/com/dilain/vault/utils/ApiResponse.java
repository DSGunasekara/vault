package com.dilain.vault.utils;

import java.time.Instant;

import com.dilain.vault.enums.ResponseStatus;

public record ApiResponse<T>(
        ResponseStatus status,
        String message,
        T data,
        Instant timestamp
) {
    public ApiResponse(ResponseStatus status, String message, T data) {
        this(status, message, data, Instant.now());
    }
}
