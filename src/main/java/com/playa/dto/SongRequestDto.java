package com.playa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongRequestDto {

    @NotNull
    private Long idUser;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150)
    private String title;

    @Size(max = 100)
    private String description;

    @NotBlank
    private String coverURL;

    @NotBlank
    private String fileURL;

    @NotBlank
    @Pattern(regexp = "^(public|private|unlisted)$", message = "La visibilidad debe ser: public, private o unlisted")
    private String visibility;
}
