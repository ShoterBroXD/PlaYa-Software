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
public class LikeId implements Serializable {

    @Column(name = "iduser")
    private Long idUser;

    @Column(name = "idsong")
    private Long idSong;

    public LikeId() {}

    public LikeId(Long idUser, Long idSong) {
        this.idUser = idUser;
        this.idSong = idSong;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LikeId likeId)) return false;
        return Objects.equals(idUser, likeId.idUser) && Objects.equals(idSong, likeId.idSong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idSong);
    }
}