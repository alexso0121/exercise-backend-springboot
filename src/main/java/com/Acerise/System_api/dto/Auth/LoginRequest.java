package com.Acerise.System_api.dto.Auth;

public record LoginRequest(
        String email,
        String encrypted_pwd
) {
}
