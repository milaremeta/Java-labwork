package com.example.labwork.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.labwork.services.client.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    private static final String USER_ID_CLAIM = "user_id";
    private static final String USERNAME_CLAIM = "username";
    private static final String EMAIL_CLAIM = "email";
    private static final long EXPIRATION_TIME_MS = TimeUnit.HOURS.toMillis(1);

    private final Set<String> invalidatedTokens = new HashSet<>();

    private final Algorithm tokenGenerator;
    private final JWTVerifier tokenVerifier;
    private final String appIssuer;

    public TokenServiceImpl(@Value("${token.secret.password}") String tokenPassword,
                            @Value("${spring.application.name}") String appName) {
        this.appIssuer = appName;
        this.tokenGenerator = Algorithm.HMAC256(tokenPassword);

        this.tokenVerifier = JWT.require(tokenGenerator)
                .withIssuer(appIssuer)
                .build();
    }

    @Override
    public String generateToken(Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MS);

        String currentUsername = "User_" + userId;
        String userEmail = "user" + userId + "@" + appIssuer.toLowerCase() + ".com";

        String token = JWT.create()
                .withIssuer(appIssuer)
                .withSubject(String.valueOf(userId))
                .withClaim(USER_ID_CLAIM, userId)
                .withClaim(USERNAME_CLAIM, currentUsername)
                .withClaim(EMAIL_CLAIM, userEmail)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(tokenGenerator);

        logger.info("Токен для UserID: {} (Термін дії: {})", userId, expiryDate);
        return token;
    }

    @Override
    public boolean isTokenValid(String token) {
        if (invalidatedTokens.contains(token)) {
            logger.warn("Токен в списку інвалідованих");
            return false;
        }

        try {
            tokenVerifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            logger.warn("Перевірка токену не вдалася: {}", exception.getMessage());
            return false;
        }
    }

    @Override
    public Integer extractUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(USER_ID_CLAIM).asInt();
        } catch (JWTVerificationException e) {
            logger.warn("Помилка вилучення ID з токену: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void invalidateToken(String token) {
        if (token != null && !token.isEmpty()) {
            invalidatedTokens.add(token);
            logger.info("Токен успішно інвалідований");
        }
    }

    @Override
    public Long getExpirationTime(String token) {
        try {
            DecodedJWT jwt = tokenVerifier.verify(token);
            return jwt.getExpiresAt().getTime();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}