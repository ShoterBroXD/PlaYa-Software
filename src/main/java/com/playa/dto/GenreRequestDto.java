package com.playa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public class GenreRequestDTO {

    @NotBlank(message = "El nombre del género no puede estar vacio")
    @Size(max = 150, message = "El nombre del género no puede tener más de 150 caracteres")
    String name;
}
