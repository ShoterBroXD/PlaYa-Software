package com.playa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsong")
    private Long idSong;

    @Column(name = "iduser", nullable = false)
    private Long idUser;

    @Column(nullable = false, length = 150)
    private String tittle;

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

    // Constructores
    public Song() {}

    public Song(Long idUser, String tittle, String description, String coverURL, String fileURL, String visibility) {
        this.idUser = idUser;
        this.tittle = tittle;
        this.description = description;
        this.coverURL = coverURL;
        this.fileURL = fileURL;
        this.visibility = visibility;
        this.uploadDate = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getIdSong() {
        return idSong;
    }

    public void setIdSong(Long idSong) {
        this.idSong = idSong;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return "Song{" +
                "idSong=" + idSong +
                ", idUser=" + idUser +
                ", tittle='" + tittle + '\'' +
                ", description='" + description + '\'' +
                ", coverURL='" + coverURL + '\'' +
                ", fileURL='" + fileURL + '\'' +
                ", visibility='" + visibility + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
