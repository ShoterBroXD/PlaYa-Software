package com.playa.dto;

import java.time.LocalDateTime;

public class UserResponseDto {
    private Long idUser;
    private String name;
    private String email;
    private String type;
    private LocalDateTime registerDate;
    private String biography;
    private Boolean premium;
    private String redSocial;

    // Constructores
    public UserResponseDto() {}

    public UserResponseDto(Long idUser, String name, String email, String type, LocalDateTime registerDate, String biography, Boolean premium, String redSocial) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.type = type;
        this.registerDate = registerDate;
        this.biography = biography;
        this.premium = premium;
        this.redSocial = redSocial;
    }

    // Getters y setters
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public String getRedSocial() {
        return redSocial;
    }

    public void setRedSocial(String redSocial) {
        this.redSocial = redSocial;
    }
}
