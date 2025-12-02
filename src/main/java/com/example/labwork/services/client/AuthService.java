package com.example.labwork.services.client;

import com.example.labwork.models.request.AuthLoginRequest;
import com.example.labwork.models.request.AuthRegisterRequest;
import com.example.labwork.models.response.AuthLoginResponse;
import com.example.labwork.models.response.AuthRegisterResponse;
import com.example.labwork.models.response.BaseResponse;

public interface AuthService {

    AuthRegisterResponse register(AuthRegisterRequest request);

    AuthLoginResponse login(AuthLoginRequest request);

    AuthLoginResponse refreshToken(String token);

    BaseResponse logout(String token);
}