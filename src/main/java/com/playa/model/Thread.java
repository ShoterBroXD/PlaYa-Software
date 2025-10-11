package com.playa.model;

import jakarta.persistence.*;import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "threads")
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


    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
}
