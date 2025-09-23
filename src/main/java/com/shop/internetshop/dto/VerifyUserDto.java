package com.shop.internetshop.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDto {

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Email musi mieć prawidłowy format")
    private String email;

    @NotBlank(message = "Kod weryfikacyjny jest wymagany")
    @Pattern(regexp = "^[0-9]{6}$", message = "Kod weryfikacyjny musi składać się z 6 cyfr")
    @Size(min = 6, max = 6, message = "Kod weryfikacyjny musi mieć dokładnie 6 cyfr")
    private String verificationCode;
}
