package com.playa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idplaylist")
    private Long idPlaylist;

    @Column(nullable = false, name = "iduser")
    private Long idUser;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, name = "creationdate")
    private LocalDateTime creationDate;

    // Constructores
    public Playlist() {}

    public Playlist(Long idUser, String name, String description) {
        this.idUser = idUser;
        this.name = name;
        this.description = description;
        this.creationDate = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Long idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "idPlaylist=" + idPlaylist +
                ", idUser=" + idUser +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
