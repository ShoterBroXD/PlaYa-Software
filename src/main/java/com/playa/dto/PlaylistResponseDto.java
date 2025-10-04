package com.playa.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PlaylistResponseDto {
    private Long idPlaylist;
    private Long idUser;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private List<SongResponseDto> songs;

    // Constructores
    public PlaylistResponseDto() {}

    public PlaylistResponseDto(Long idPlaylist, Long idUser, String name, String description, LocalDateTime creationDate) {
        this.idPlaylist = idPlaylist;
        this.idUser = idUser;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }

    // Getters y setters
    public Long getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Long idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<SongResponseDto> getSongs() {
        return songs;
    }

    public void setSongs(List<SongResponseDto> songs) {
        this.songs = songs;
    }
}
