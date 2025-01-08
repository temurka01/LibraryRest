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
    @Size(min = 6, max = 12, message = "Пароль должен быть от 6 до 12 символов")
    private String password;
}
