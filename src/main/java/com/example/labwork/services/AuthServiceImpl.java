package com.example.labwork.services;

import com.example.labwork.models.request.AuthLoginRequest;
import com.example.labwork.models.request.AuthRegisterRequest;
import com.example.labwork.models.response.AuthLoginResponse;
import com.example.labwork.models.response.AuthRegisterResponse;
import com.example.labwork.models.response.BaseResponse;
import com.example.labwork.services.client.AuthService;
import com.example.labwork.services.client.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Period;
import java.time.LocalDate;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private TokenService tokenService;

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

    private BaseResponse errorBaseResponse(String message) {
        BaseResponse response = new BaseResponse();
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

        Integer userId = 1;
        String token = tokenService.generateToken(userId);

        AuthRegisterResponse response = new AuthRegisterResponse();
        response.setSuccess(true);
        response.setMessage("Реєстрація успішна.");
        response.setToken(token);
        logger.info("Користувача '{}' зареєстровано успішно. Токен згенеровано.", request.getUsername());

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

        Integer userId = 1;
        String token = tokenService.generateToken(userId);

        AuthLoginResponse response = new AuthLoginResponse();
        response.setSuccess(true);
        response.setMessage("Авторизація успішна.");
        response.setToken(token);
        logger.info("Користувач '{}' успішно авторизований. Токен згенеровано.", request.getUsername());

        return response;
    }

    @Override
    public AuthLoginResponse refreshToken(String token) {
        logger.info("Отримано запит на оновлення токену.");

        Integer userId = tokenService.extractUserId(token);

        if (userId == null) {
            logger.warn("Неможливо витягти ID користувача з токену.");
            return errorLoginResponse("Недійсний токен.");
        }

        tokenService.invalidateToken(token);

        String newToken = tokenService.generateToken(userId);

        AuthLoginResponse resp = new AuthLoginResponse();
        resp.setSuccess(true);
        resp.setMessage("token refreshed");
        resp.setToken(newToken);
        logger.info("Токен оновлено для User ID: {}", userId);
        return resp;
    }

    @Override
    public BaseResponse logout(String token) {
        tokenService.invalidateToken(token);

        BaseResponse resp = new BaseResponse();
        resp.setSuccess(true);
        resp.setMessage("Logout successful");
        return resp;
    }
}