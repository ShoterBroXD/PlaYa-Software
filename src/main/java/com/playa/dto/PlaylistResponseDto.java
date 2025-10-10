package com.playa.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class PlaylistResponseDto {
    private Long idPlaylist;
    private Long idUser;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private List<SongResponseDto> songs;
}
