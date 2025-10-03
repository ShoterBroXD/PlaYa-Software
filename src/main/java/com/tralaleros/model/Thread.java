package com.tralaleros.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "thread")
public class Thread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idthread")
    private Long idThread;

    @Column(nullable = false, name = "iduser")
    private Long idUser;

    @Column(nullable = false, name = "id_community")
    private Long idCommunity;

    @Column(length = 200)
    private String title;

    @Column(name = "creationdate")
    private LocalDateTime creationDate;

    // Constructores
    public Thread() {}

    // Getters y Setters
    public Long getIdThread() { return idThread; }
    public void setIdThread(Long idThread) { this.idThread = idThread; }

    public Long getIdUser() { return idUser; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public Long getIdCommunity() { return idCommunity; }
    public void setIdCommunity(Long idCommunity) { this.idCommunity = idCommunity; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
}
