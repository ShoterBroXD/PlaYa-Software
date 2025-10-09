package com.playa.dto;

public class CommunityRequestDto {
    private String name;
    private String description;

    // Constructores
    public CommunityRequestDto() {}

    public CommunityRequestDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters y setters
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
