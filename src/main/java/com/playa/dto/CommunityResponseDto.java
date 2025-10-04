package com.playa.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CommunityResponseDto {
    private Long idCommunity;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private List<UserResponseDto> members;

    // Constructores
    public CommunityResponseDto() {}

    public CommunityResponseDto(Long idCommunity, String name, String description, LocalDateTime creationDate) {
        this.idCommunity = idCommunity;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }

    // Getters y setters
    public Long getIdCommunity() {
        return idCommunity;
    }

    public void setIdCommunity(Long idCommunity) {
        this.idCommunity = idCommunity;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<UserResponseDto> getMembers() {
        return members;
    }

    public void setMembers(List<UserResponseDto> members) {
        this.members = members;
    }
}
