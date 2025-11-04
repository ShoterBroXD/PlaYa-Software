package com.playa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PlaySongRequestDto {

    @NotNull(message = "El ID de la canción es obligatorio")
    private Long idSong;

    private String context;
    private Long idcontext;
    private List<Long> idsqueue;
}