package com.playa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcomment")
    private Long idComment;

    // Relación con User (idUser)

    // Relación con Song (idSong)
    // Relación con Comment (parentComment) para respuestas a comentarios

    @Column(nullable = false, name = "iduser")
    private Long idUser;

    @Column(nullable = false, name = "idsong")
    private Long idSong;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "parentcomment")
    private Long parentComment;

    @Column(nullable = false)
    private LocalDateTime date;

    // Constructores
    public Comment() {}

}