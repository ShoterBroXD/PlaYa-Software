package com.playa.dto;

public class JoinCommunityDto {
    private Long idUser;

    // Constructores
    public JoinCommunityDto() {}

    public JoinCommunityDto(Long idUser) {
        this.idUser = idUser;
    }

    // Getters y setters
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
}
