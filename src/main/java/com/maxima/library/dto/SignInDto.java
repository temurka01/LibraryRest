package com.maxima.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignInDto {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 4, max = 12, message = "Пароль должен быть от 4 до 12 символов")
    private String password;
}