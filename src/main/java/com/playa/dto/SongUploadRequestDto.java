package com.playa.dto;

import com.playa.model.Genre;
import jakarta.validation.constraints.*;
import lombok.Data;


import java.util.Set;

@Data
public class SongUploadRequestDto {
    @NotBlank(message = "El título de la canción no puede estar vacío")
    @Size(max = 150, message = "El título de la canción no puede tener más de 150 caracteres")
    private String title;

    private String description;

    @NotBlank(message = "La URL de la portada no puede estar vacía")
    private String coverURL;

    @NotBlank(message = "La URL del archivo no puede estar vacía")
    private String fileURL;

    @Pattern(regexp = "public|private|unlisted", message = "La visibilidad debe ser 'PUBLIC', 'PRIVATE' o 'UNLISTED'")
    private String visibility="public";

    @NotNull(message = "Debe seleccionar un género")
    private Long idgenre;
}
