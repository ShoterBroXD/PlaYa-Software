package com.playa.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "song")
@Data
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsong")
    private Long idSong;


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
    @JoinColumn(name = "iduser",referencedColumnName = "iduser")
    private User user;

    @ManyToOne
    @JoinColumn(name="playlist_id", nullable=true)
    private Playlist playlist;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "song_genre",
            joinColumns = @JoinColumn(name = "idsong"),
            inverseJoinColumns = @JoinColumn(name = "idgenre")
    )
    private Set<Genre> genres = new HashSet<>();

    public Long getIdUser() { return user.getIdUser();}

    // Constructores
    public Song() {}

    public Song(Long idUser, String title, Float duration, String description, String coverURL, String fileURL, String visibility) {
        this.user= new User();
        this.user.setIdUser(idUser);
        this.title = title;
        this.duration = duration;
        this.description = description;
        this.coverURL = coverURL;
        this.fileURL = fileURL;
        this.visibility = visibility;
        this.uploadDate = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "Song{" +
                "idSong=" + idSong +
                ", idUser=" + user.getIdUser() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", coverURL='" + coverURL + '\'' +
                ", fileURL='" + fileURL + '\'' +
                ", visibility='" + visibility + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }

}
