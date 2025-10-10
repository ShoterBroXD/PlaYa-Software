package com.playa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcomment")
    private Long idComment;

    //@Column(nullable = false, name = "iduser")
    //private Long idUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser")
    private User user;

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

    // Getters y Setters
    public Long getIdComment() { return idComment; }
    public void setIdComment(Long idComment) { this.idComment = idComment; }

    public Long getIdUser() { return user.getIdUser(); }
    public void setIdUser(Long idUser) {this.user.setIdUser(idUser);}

    public Long getIdSong() { return idSong; }
    public void setIdSong(Long idSong) { this.idSong = idSong; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getParentComment() { return parentComment; }
    public void setParentComment(Long parentComment) { this.parentComment = parentComment; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
