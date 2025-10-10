package com.playa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsong")
    private Long idSong;

    @Column(name = "iduser", nullable = false)
    private Long idUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false, length = 150, name = "tittle")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, name = "coverurl", columnDefinition = "TEXT")
    private String coverURL;

    @Column(nullable = false, name = "fileurl", columnDefinition = "TEXT")
    private String fileURL;

    @Column(nullable = false, length = 20)
    private String visibility; // 'public', 'private', 'unlisted'

    @Column(nullable = false, name = "uploaddate")
    private LocalDateTime uploadDate;
}
