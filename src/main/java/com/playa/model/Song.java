package com.playa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    //@Column(name = "iduser", nullable = false)
    //private Long idUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser", updatable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false)
    private float duration;

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

    @ManyToOne
    @JoinColumn(name="playlist_id", nullable=true)
    private Playlist playlist;

    @ManyToOne
    @JoinTable(name = "song_genre",
            joinColumns = @JoinColumn(name = "id_song"),
            inverseJoinColumns = @JoinColumn(name = "id_genre"))
    private Genre genre;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments= new ArrayList<>();

}
