package com.playa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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

}
