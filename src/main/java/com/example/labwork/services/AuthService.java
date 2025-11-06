package com.example.labwork.services;

import com.example.labwork.models.request.AuthLoginRequest;
import com.example.labwork.models.request.AuthRegisterRequest;
import com.example.labwork.models.response.AuthLoginResponse;
import com.example.labwork.models.response.AuthRegisterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Period;
import java.time.LocalDate;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private AuthRegisterResponse errorResponse(String message) {
        AuthRegisterResponse response = new AuthRegisterResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    private AuthLoginResponse errorLoginResponse(String message) {
        AuthLoginResponse response = new AuthLoginResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^(\\+380|0)\\d{9}$");
    }

    public AuthRegisterResponse register(AuthRegisterRequest request) {
        logger.info("Запит на реєстрацію користувача");

        if (isNullOrEmpty(request.getUsername())) {
            logger.warn("Username відсутній");
            return errorResponse("Поле 'username' є обов’язковим.");
        }
        if (isNullOrEmpty(request.getPassword())) {
            logger.warn("Password відсутній");
            return errorResponse("Поле 'password' є обов’язковим.");
        }
        if (isNullOrEmpty(request.getEmail())) {
            logger.warn("Email відсутній");
            return errorResponse("Поле 'email' є обов’язковим.");
        }
        if (request.getBirthday() == null) {
            logger.warn("Birthday відсутній");
            return errorResponse("Поле 'birthday' є обов’язковим.");
        }

        int age = Period.between(request.getBirthday(), LocalDate.now()).getYears();
        if (age < 14) {
            logger.warn("Користувач '{}' недостатнього віку: {} років", request.getUsername(), age);
            return errorResponse("Реєстрація доступна для користувачів старше 14 років.");
        }

        if (!request.getEmail().contains("@")) {
            logger.warn("Некоректний email: {}", request.getEmail());
            return errorResponse("Некоректний формат email.");
        }
        if (request.getPassword().length() < 8) {
            logger.warn("Короткий пароль користувача '{}'", request.getUsername());
            return errorResponse("Пароль має містити щонайменше 8 символів.");
        }

        if (!isNullOrEmpty(request.getPhoneNumber()) && !isValidPhoneNumber(request.getPhoneNumber())) {
            logger.warn("Некоректний номер телефону: {}", request.getPhoneNumber());
            return errorResponse("Некоректний формат номера телефону. Використовуйте +380 або 0XXXXXXXXX.");
        }

        AuthRegisterResponse response = new AuthRegisterResponse();
        response.setSuccess(true);
        response.setMessage("Реєстрація успішна.");
        logger.info("Користувача '{}' зареєстровано успішно", request.getUsername());

        return response;
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        logger.info("Запит на авторизацію користувача");

        if (isNullOrEmpty(request.getUsername())) {
            logger.warn("Username відсутній");
            return errorLoginResponse("Поле 'username' є обов’язковим.");
        }
        if (isNullOrEmpty(request.getPassword())) {
            logger.warn("Password відсутній");
            return errorLoginResponse("Поле 'password' є обов’язковим.");
        }

        AuthLoginResponse response = new AuthLoginResponse();
        response.setSuccess(true);
        response.setMessage("Авторизація успішна.");
        response.setToken("mock-jwt-token-123");

        logger.info("Авторизація користувача '{}' успішна", request.getUsername());
        return response;
    }
}