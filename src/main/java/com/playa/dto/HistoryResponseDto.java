package com.playa.dto;

import com.playa.model.Song;
import com.playa.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryResponseDto {
    private User idUser;
    private Song idSong;

    // Constructores, getters y setters
    public HistoryResponseDto(User idUser, Song idSong) {
        this.idUser = idUser;
        this.idSong = idSong;
    }
}
