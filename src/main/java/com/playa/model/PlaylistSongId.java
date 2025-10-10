package com.playa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PlaylistSongId implements Serializable {
    @Column(name = "idplaylist")
    private Long idPlaylist;

    @Column(name = "idsong")
    private Long idSong;

    // Constructores
    public PlaylistSongId() {}

    public PlaylistSongId(Long idPlaylist, Long idSong) {
        this.idPlaylist = idPlaylist;
        this.idSong = idSong;
    }

    // Getters y Setters
    public Long getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Long idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public Long getIdSong() {
        return idSong;
    }

    public void setIdSong(Long idSong) {
        this.idSong = idSong;
    }

    // equals y hashCode son necesarios para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistSongId that = (PlaylistSongId) o;
        return Objects.equals(idPlaylist, that.idPlaylist) && Objects.equals(idSong, that.idSong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlaylist, idSong);
    }
}
