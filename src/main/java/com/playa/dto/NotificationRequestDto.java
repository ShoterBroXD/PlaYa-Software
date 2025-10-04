package com.playa.dto;

import java.time.LocalDateTime;

public class NotificationRequestDto {
    private Long idUser;
    private String content;
    private LocalDateTime date;

    // Constructores
    public NotificationRequestDto() {}

    public NotificationRequestDto(Long idUser, String content, LocalDateTime date) {
        this.idUser = idUser;
        this.content = content;
        this.date = date;
    }

    // Getters y setters
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
