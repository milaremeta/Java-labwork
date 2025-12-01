package com.example.labwork.controllers;

import com.example.labwork.models.request.AuthLoginRequest;
import com.example.labwork.models.request.AuthRegisterRequest;
import com.example.labwork.models.response.AuthLoginResponse;
import com.example.labwork.models.response.AuthRegisterResponse;
import com.example.labwork.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {

    @Autowired
    private AuthService authService;

    @Override
    public AuthLoginResponse login(AuthLoginRequest request) {
        return authService.login(request);
    }

    @Override
    public AuthRegisterResponse register(AuthRegisterRequest request) {
        return authService.register(request);
    }
}