package com.shop.internetshop.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Email musi mieć prawidłowy format")
    @Size(max = 100, message = "Email może mieć maksymalnie 100 znaków")
    private String email;

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 8, max = 128, message = "Hasło musi mieć od 8 do 128 znaków")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Hasło musi zawierać co najmniej jedną małą literę, jedną wielką literę i jedną cyfrę"
    )
    private String password;

    @NotBlank(message = "Username jest wymagany")
    @Size(min = 3, max = 50, message = "Username musi mieć od 3 do 50 znaków")
    @Pattern(
            regexp = "^[a-zA-Z0-9_-]+$",
            message = "Username może zawierać tylko litery, cyfry, podkreślnik (_) i myślnik (-)"
    )
    private String username;

}
