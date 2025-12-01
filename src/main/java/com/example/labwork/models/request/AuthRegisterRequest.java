package com.example.labwork.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class AuthRegisterRequest {

    @NotBlank(message = "Username не може бути порожнім.")
    @Size(min = 4, max = 25, message = "Username має бути від 4 до 25 символів.")
    private String username;

    @NotBlank(message = "Password не може бути порожнім.")
    @Size(min = 8, message = "Пароль має містити щонайменше 8 символів.")
    private String password;

    @NotBlank(message = "Email не може бути порожнім.")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Некоректний формат email.")
    private String email;

    @NotNull(message = "Дата народження є обов’язковою.")
    @Past(message = "Дата народження має бути в минулому.")
    private LocalDate birthday;

    @Pattern(regexp = "^$|^(\\+380|0)\\d{9}$", message = "Некоректний формат номера телефону.")
    private String phoneNumber;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}