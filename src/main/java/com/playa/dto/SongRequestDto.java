package com.playa.dto;

import com.playa.model.Genre;

import java.util.Set;

public class SongRequestDto {
    private Long idUser;
    private String title;
    private String description;
    private String coverURL;
    private String fileURL;
    private String visibility;
    public Genre genre;
    public Set<Genre> genres;

    // Constructores
    public SongRequestDto() {}

    public SongRequestDto(Long idUser, String title, String description, String coverURL, String fileURL, String visibility) {
        this.idUser = idUser;
        this.title = title;
        this.description = description;
        this.coverURL = coverURL;
        this.fileURL = fileURL;
        this.visibility = visibility;
    }

    // Getters y Setters
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



    public Set<Genre> getGenres(){
        return genres;
    }

    public Long getGenreId(){return genre.getIdGenre();}

}
