package com.playa.dto;

import java.time.LocalDateTime;

public class SongResponseDto {
    private Long idSong;
    private Long idUser;
    private String title;
    private String description;
    private String coverURL;
    private String fileURL;
    private String visibility;
    private LocalDateTime uploadDate;

    // Constructores
    public SongResponseDto() {}

    public SongResponseDto(Long idSong, Long idUser, String title, String description, String coverURL, String fileURL, String visibility, LocalDateTime uploadDate) {
        this.idSong = idSong;
        this.idUser = idUser;
        this.title = title;
        this.description = description;
        this.coverURL = coverURL;
        this.fileURL = fileURL;
        this.visibility = visibility;
        this.uploadDate = uploadDate;
    }

    // Getters y setters
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
