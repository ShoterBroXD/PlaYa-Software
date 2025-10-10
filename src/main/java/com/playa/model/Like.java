package com.playa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"like\"")
public class Like {

    @EmbeddedId
    private LikeId id;

    @Column(nullable = false)
    private LocalDateTime date;

    // Constructores
    public Like() {}

    // Getters y Setters
    public LikeId getId() { return id; }
    public void setId(LikeId id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}

@Embeddable
class LikeId implements java.io.Serializable {
    @Column(name = "iduser")
    private Long idUser;

    @Column(name = "idsong")
    private Long idSong;

    // Constructores
    public LikeId() {}

    public LikeId(Long idUser, Long idSong) {
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
        LikeId likeId = (LikeId) o;
        return idUser.equals(likeId.idUser) && idSong.equals(likeId.idSong);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idUser, idSong);
    }
}
