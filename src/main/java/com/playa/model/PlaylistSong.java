package com.playa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_song")
public class PlaylistSong {

    @EmbeddedId
    private PlaylistSongId id;

    @Column
    private LocalDateTime date;

    // Constructores
    public PlaylistSong() {}

    public PlaylistSong(Long idPlaylist, Long idSong) {
        this.id = new PlaylistSongId(idPlaylist, idSong);
        this.date = LocalDateTime.now();
    }

    // Getters y Setters
    public PlaylistSongId getId() {
        return id;
    }

    public void setId(PlaylistSongId id) {
        this.id = id;
    }

    public Long getIdPlaylist() {
        return id != null ? id.getIdPlaylist() : null;
    }

    public void setIdPlaylist(Long idPlaylist) {
        if (this.id == null) {
            this.id = new PlaylistSongId();
        }
        this.id.setIdPlaylist(idPlaylist);
    }

    public Long getIdSong() {
        return id != null ? id.getIdSong() : null;
    }

    public void setIdSong(Long idSong) {
        if (this.id == null) {
            this.id = new PlaylistSongId();
        }
        this.id.setIdSong(idSong);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
