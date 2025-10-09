package com.playa.dto;

public class PlaylistRequestDto {
    private Long idUser;
    private String name;
    private String description;

    // Constructores
    public PlaylistRequestDto() {}

    public PlaylistRequestDto(Long idUser, String name, String description) {
        this.idUser = idUser;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
