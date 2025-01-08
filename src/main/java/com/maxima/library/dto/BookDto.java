package com.maxima.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookDto {
    @NotBlank
    private String title;
    @NotBlank
    private String name;
    @NotBlank
    private String country;
    @NotNull
    private Integer releaseYear;
    @Min(value = 1L)
    @NotNull
    private Integer amount;
}
