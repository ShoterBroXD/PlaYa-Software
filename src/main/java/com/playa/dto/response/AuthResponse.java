package com.playa.dto.response;

public record AuthResponse(
    String token,
    String email,
    String name
) {}
