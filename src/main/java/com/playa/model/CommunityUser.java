package com.playa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "community_user")
public class CommunityUser {

    @EmbeddedId
    private CommunityUserId id;

    @Column(name = "joindate", nullable = false)
    private LocalDateTime joinDate;

    // Constructores
    public CommunityUser() {}

    public CommunityUser(Long idCommunity, Long idUser) {
        this.id = new CommunityUserId(idCommunity, idUser);
        this.joinDate = LocalDateTime.now();
    }

    // Getters y Setters
    public CommunityUserId getId() {
        return id;
    }

    public void setId(CommunityUserId id) {
        this.id = id;
    }

    public Long getIdCommunity() {
        return id != null ? id.getIdCommunity() : null;
    }

    public void setIdCommunity(Long idCommunity) {
        if (this.id == null) {
            this.id = new CommunityUserId();
        }
        this.id.setIdCommunity(idCommunity);
    }

    public Long getIdUser() {
        return id != null ? id.getIdUser() : null;
    }

    public void setIdUser(Long idUser) {
        if (this.id == null) {
            this.id = new CommunityUserId();
        }
        this.id.setIdUser(idUser);
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }
}
