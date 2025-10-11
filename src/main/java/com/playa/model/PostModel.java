package com.playa.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "posts")
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
}
