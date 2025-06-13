package com.flight.api.constant;

public class SecurityConstants {
    public static final String JWT_SECRET = "your-secret-key-here-must-be-at-least-32-characters";
    public static final long JWT_EXPIRATION_MS = 86400000; // 24 hours
    public static final String JWT_HEADER_STRING = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    private SecurityConstants() {
        // Private constructor to prevent instantiation
    }
} 