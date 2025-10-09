package com.playa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpost")
    private Long idPost;

    @Column(nullable = false, name = "idthread")
    private Long idThread;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "postdate")
    private LocalDateTime postDate;

    // Constructores
    public Post() {}

    // Getters y Setters
    public Long getIdPost() { return idPost; }
    public void setIdPost(Long idPost) { this.idPost = idPost; }

    public Long getIdThread() { return idThread; }
    public void setIdThread(Long idThread) { this.idThread = idThread; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getPostDate() { return postDate; }
    public void setPostDate(LocalDateTime postDate) { this.postDate = postDate; }
}
