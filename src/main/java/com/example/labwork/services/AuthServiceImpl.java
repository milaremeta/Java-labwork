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
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

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

    @Override
    public AuthRegisterResponse register(AuthRegisterRequest request) {
        logger.info("Запит на реєстрацію користувача: {}", request.getUsername());

        int age = Period.between(request.getBirthday(), LocalDate.now()).getYears();
        if (age < 14) {
            logger.warn("Користувач '{}' недостатнього віку: {} років", request.getUsername(), age);
            return errorResponse("Реєстрація доступна для користувачів старше 14 років.");
        }

        AuthRegisterResponse response = new AuthRegisterResponse();
        response.setSuccess(true);
        response.setMessage("Реєстрація успішна.");
        logger.info("Користувача '{}' зареєстровано успішно", request.getUsername());

        return response;
    }

    @Override
    public AuthLoginResponse login(AuthLoginRequest request) {
        logger.info("Запит на авторизацію користувача: {}", request.getUsername());

        final String correctUsername = "correctusername";
        final String correctPassword = "correctpassword123";

        if (!request.getUsername().equals(correctUsername) || !request.getPassword().equals(correctPassword)) {
            logger.warn("Неправильні облікові дані для {}", request.getUsername());
            return errorLoginResponse("Неправильний username або пароль.");
        }

        AuthLoginResponse response = new AuthLoginResponse();
        response.setSuccess(true);
        response.setMessage("Авторизація успішна.");
        response.setToken("mock-jwt-token-123");
        logger.info("Користувач '{}' успішно авторизований", request.getUsername());

        return response;
    }
}