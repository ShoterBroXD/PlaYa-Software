package com.tralaleros.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "songs_history")
public class History {

    @EmbeddedId
    private HistoryId id;

    @Column(nullable = false, name = "dateplayed")
    private LocalDateTime datePlayed;

    // Constructores
    public History() {}

    // Getters y Setters
    public HistoryId getId() { return id; }
    public void setId(HistoryId id) { this.id = id; }

    public LocalDateTime getDatePlayed() { return datePlayed; }
    public void setDatePlayed(LocalDateTime datePlayed) { this.datePlayed = datePlayed; }
}

@Embeddable
class HistoryId implements java.io.Serializable {
    @Column(name = "iduser")
    private Long idUser;

    @Column(name = "idsong")
    private Long idSong;

    // Constructores
    public HistoryId() {}

    public HistoryId(Long idUser, Long idSong) {
        this.idUser = idUser;
        this.idSong = idSong;
    }

    // Getters y Setters
    public Long getIdUser() { return idUser; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public Long getIdSong() { return idSong; }
    public void setIdSong(Long idSong) { this.idSong = idSong; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryId historyId = (HistoryId) o;
        return idUser.equals(historyId.idUser) && idSong.equals(historyId.idSong);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idUser, idSong);
    }
}
