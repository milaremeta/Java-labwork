package com.example.labwork.controllers.client;

import com.example.labwork.models.request.AuthLoginRequest;
import com.example.labwork.models.request.AuthRegisterRequest;
import com.example.labwork.models.response.AuthLoginResponse;
import com.example.labwork.models.response.AuthRegisterResponse;
import com.example.labwork.models.response.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

@RequestMapping("/auth")
public interface AuthController {

    @PostMapping("/login")
    AuthLoginResponse login(@Valid @RequestBody AuthLoginRequest request);

    @PostMapping("/register")
    AuthRegisterResponse register(@Valid @RequestBody AuthRegisterRequest request);

    @PostMapping("/refresh")
    AuthLoginResponse refreshToken(String oldToken);

    @PostMapping("/invalidate")
    BaseResponse invalidateToken(String token);
}