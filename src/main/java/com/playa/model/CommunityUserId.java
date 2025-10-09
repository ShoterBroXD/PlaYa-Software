package com.playa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommunityUserId implements Serializable {
    @Column(name = "id_community")
    private Long idCommunity;

    @Column(name = "iduser")
    private Long idUser;

    // Constructores
    public CommunityUserId() {}

    public CommunityUserId(Long idCommunity, Long idUser) {
        this.idCommunity = idCommunity;
        this.idUser = idUser;
    }

    // Getters y Setters
    public Long getIdCommunity() {
        return idCommunity;
    }

    public void setIdCommunity(Long idCommunity) {
        this.idCommunity = idCommunity;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    // equals y hashCode son necesarios para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunityUserId that = (CommunityUserId) o;
        return Objects.equals(idCommunity, that.idCommunity) && Objects.equals(idUser, that.idUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCommunity, idUser);
    }
}
