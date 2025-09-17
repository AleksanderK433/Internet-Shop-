package com.shop.internetshop.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDto(
        @NotBlank(message = "Nazwa użytkownika lub email jest wymagany")
        String usernameOrEmail,

        @NotBlank(message = "Hasło jest wymagane")
        @Size(min = 6)
        String password
) { }