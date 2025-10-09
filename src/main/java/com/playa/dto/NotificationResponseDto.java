package com.playa.dto;

import java.time.LocalDateTime;

public class NotificationResponseDto {
    private Long idNotification;
    private Long idUser;
    private String content;
    private LocalDateTime date;
    private Boolean read;

    // Constructores
    public NotificationResponseDto() {}

    public NotificationResponseDto(Long idNotification, Long idUser, String content, LocalDateTime date, Boolean read) {
        this.idNotification = idNotification;
        this.idUser = idUser;
        this.content = content;
        this.date = date;
        this.read = read;
    }

    // Getters y setters
    public Long getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(Long idNotification) {
        this.idNotification = idNotification;
    }

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

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}
