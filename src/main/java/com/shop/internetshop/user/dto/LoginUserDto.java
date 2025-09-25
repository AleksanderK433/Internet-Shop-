package com.shop.internetshop.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Email musi mieć prawidłowy format")
    private String email;

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 1, message = "Hasło nie może być puste")
    private String password;

}
