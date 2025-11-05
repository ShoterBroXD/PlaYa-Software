package com.playa.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class LikeId implements java.io.Serializable {
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
