package com.shop.internetshop.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDto(
        @NotBlank(message = "Nazwa użytkownika jest wymagana")
        @Size(min = 3, max = 20)
        String username,

        @Email(message = "Proszę podać prawidłowy adres e-mail")
        @NotBlank(message = "Adres e-mail jest wymagany")
        String email,

        @NotBlank(message = "Hasło jest wymagane")
        @Size(min = 6, message = "Hasło musi mieć co najmniej 6 znaków")
        String password,

        @NotBlank(message = "Proszę potwierdzić hasło")
        String confirmPassword
) {
}
