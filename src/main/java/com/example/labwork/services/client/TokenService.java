package com.example.labwork.services.client;

public interface TokenService {

    String generateToken(Integer userId);

    boolean isTokenValid(String token);

    Integer extractUserId(String token);

    void invalidateToken(String token);

    Long getExpirationTime(String token);
}