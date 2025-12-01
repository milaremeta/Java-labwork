package com.example.labwork.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthLoginRequest {

    @NotBlank(message = "Username не може бути порожнім.")
    @Size(min = 4, max = 50, message = "Username має бути від 4 до 50 символів.")
    private String username;

    @NotBlank(message = "Пароль не може бути порожнім.")
    @Size(min = 8, message = "Пароль має містити щонайменше 8 символів.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}