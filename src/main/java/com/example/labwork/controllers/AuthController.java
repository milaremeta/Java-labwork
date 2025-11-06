package com.example.labwork.controllers;

import com.example.labwork.models.request.AuthLoginRequest;
import com.example.labwork.models.request.AuthRegisterRequest;
import com.example.labwork.models.response.AuthLoginResponse;
import com.example.labwork.models.response.AuthRegisterResponse;
import com.example.labwork.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthRegisterResponse register(@RequestBody AuthRegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthLoginResponse login(@RequestBody AuthLoginRequest request) {
        return authService.login(request);
    }
}