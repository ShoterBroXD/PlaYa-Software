package com.playa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcomment")
    private Long idComment;

    @Column(nullable = false, name = "iduser")
    private Long idUser;

    //@Column(nullable = false, name = "idsong")
    //private Long idSong;

    @ManyToOne()
    @JoinColumn(name = "idsong")
    private Song song;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "parentcomment")
    private Long parentComment;

    @Column(nullable = false)
    private LocalDateTime date;

    // Getters y Setters
    public Long getIdComment() { return idComment; }
    public void setIdComment(Long idComment) { this.idComment = idComment; }

    public Long getIdUser() { return this.getIdUser(); }
    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public Long getIdSong() { return song.getIdSong(); }
    public void setIdSong(Long idSong) { this.song.setIdSong(idSong); }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getParentComment() { return parentComment; }
    public void setParentComment(Long parentComment) { this.parentComment = parentComment; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
