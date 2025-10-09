package com.playa.dto;

public class AddSongToPlaylistDto {
    private Long idSong;

    // Constructores
    public AddSongToPlaylistDto() {}

    public AddSongToPlaylistDto(Long idSong) {
        this.idSong = idSong;
    }

    // Getters y setters
    public Long getIdSong() {
        return idSong;
    }

    public void setIdSong(Long idSong) {
        this.idSong = idSong;
    }
}
