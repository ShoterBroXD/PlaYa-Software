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

    public PlaylistResponseDto(Long idPlaylist, Long idUser, String name, String description, LocalDateTime creationDate, List<SongResponseDto> songs) {
        this.idPlaylist = idPlaylist;
        this.idUser = idUser;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.songs = songs;
    }
}
