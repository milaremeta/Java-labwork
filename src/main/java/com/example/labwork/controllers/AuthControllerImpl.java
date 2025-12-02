package com.example.labwork.controllers;

import com.example.labwork.controllers.client.AuthController;
import com.example.labwork.models.request.AuthLoginRequest;
import com.example.labwork.models.request.AuthRegisterRequest;
import com.example.labwork.models.response.AuthLoginResponse;
import com.example.labwork.models.response.AuthRegisterResponse;
import com.example.labwork.models.response.BaseResponse;
import com.example.labwork.security.TokenValidationRequired;
import com.example.labwork.services.client.AuthService;
import com.example.labwork.services.client.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthControllerImpl implements AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @Override
    public AuthLoginResponse login(@RequestBody AuthLoginRequest request) {
        return authService.login(request);
    }

    @Override
    public AuthRegisterResponse register(@RequestBody AuthRegisterRequest request) {
        return authService.register(request);
    }

    @Override
    @TokenValidationRequired
    public AuthLoginResponse refreshToken(@RequestHeader("Authorization") String authHeader) {
        String oldToken = authHeader.substring(7);
        return authService.refreshToken(oldToken);
    }

    @Override
    @TokenValidationRequired
    public BaseResponse invalidateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return authService.logout(token);
    }
}