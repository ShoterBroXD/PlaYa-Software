package com.playa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RepeatModeRequestDto {

    @NotBlank(message = "El modo de repetición es obligatorio")
    @Pattern(regexp = "none|one|all", message = "Modo inválido. Usa: none, one, all")
    private String mode;
}