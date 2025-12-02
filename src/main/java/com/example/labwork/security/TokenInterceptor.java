package com.example.labwork.security;

import com.example.labwork.services.client.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.IOException;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
    private static final String SECURITY_TOKEN_HEADER_NAME = "Authorization";

    private static final String USER_ID_REQUEST_ATTRIBUTE = "userId";

    private final TokenService tokenService;

    @Autowired
    public TokenInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (!handlerMethod.hasMethodAnnotation(TokenValidationRequired.class)) {
            return true;
        }

        String authHeader = request.getHeader(SECURITY_TOKEN_HEADER_NAME);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Відсутній або некоректний заголовок Authorization.");
            sendUnauthorizedResponse(response, "Токен відсутній або неправильний формат.", "MISSING_TOKEN");
            return false;
        }

        String token = authHeader.substring(7);

        if (!tokenService.isTokenValid(token)) {
            logger.warn("Недійсний або прострочений токен.");
            sendUnauthorizedResponse(response, "Недійсний токен.", "INVALID_TOKEN");
            return false;
        }

        Integer userId = tokenService.extractUserId(token);
        if (userId == null) {
            logger.error("Не вдалося витягти User ID з валідного токена.");
            sendUnauthorizedResponse(response, "Помилка вилучення даних користувача.", "SERVER_ERROR");
            return false;
        }

        request.setAttribute(USER_ID_REQUEST_ATTRIBUTE, userId);
        logger.info("Доступ надано для User ID: {}", userId);

        return true;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message, String error) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        String json = String.format("{\"message\":\"%s\", \"error\":\"%s\"}", message, error);

        response.getWriter().write(json);
        response.getWriter().flush();
    }
}