package com.example.labwork.services;

import com.example.labwork.services.client.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenServiceImplTest {

    private TokenService tokenService;
    private final Integer TEST_USER_ID = 99;
    private final String TEST_APP_NAME = "TestLabworkApp";
    private final String TEST_SECRET = "secure_lab_token_key_6428567890lhvskfghijklmnop";

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl(TEST_SECRET, TEST_APP_NAME);
    }

    @Test
    void testGenerate() {
        String token = tokenService.generateToken(TEST_USER_ID);

        assertNotNull(token, "Токен не повинен бути null.");
        assertFalse(token.isEmpty(), "Токен не повинен бути порожнім.");

        Integer userIdFromToken = tokenService.extractUserId(token);
        assertEquals(TEST_USER_ID, userIdFromToken, "UserId має бути правильно витягнуто.");

        assertNotNull(tokenService.getExpirationTime(token), "Токен повинен мати термін дії.");
    }

    @Test
    void testIsValidWhenGenerated() {
        String token = tokenService.generateToken(TEST_USER_ID);
        assertTrue(tokenService.isTokenValid(token), "Токен має бути валідним після генерації.");
    }

    @Test
    void testIsNotValidWhenInvalidated() {
        String token = tokenService.generateToken(TEST_USER_ID);
        assertTrue(tokenService.isTokenValid(token), "Токен повинен бути дійсним.");

        tokenService.invalidateToken(token);

        boolean validAfter = tokenService.isTokenValid(token);

        assertFalse(validAfter, "Токен повинен бути недійсним після інвалідації.");
    }

    @Test
    void testIsNotValidWhenWrongKey() {
        final String WRONG_SECRET = "invalid_lab_token_key_alsnddjj9876543210zyxw";

        TokenService rogueService = new TokenServiceImpl(WRONG_SECRET, "TestLabworkApp");

        String validToken = tokenService.generateToken(TEST_USER_ID);

        boolean isValidWithWrongKey = rogueService.isTokenValid(validToken);

        assertFalse(isValidWithWrongKey, "Токен з іншим ключем має бути недійсним.");
    }
}