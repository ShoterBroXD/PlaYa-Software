package com.playa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idnotification")
    private Long idNotification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=true)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, name = "read")
    private Boolean read = false;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String type;

    // Constructores
    public Notification() {}

    public Notification(Long idUser, String content) {
        this.user.setIdUser(idUser);
        this.content = content;
        this.read = false;
        this.date = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(Long idNotification) {
        this.idNotification = idNotification;
    }

    public Long getIdUser() {
        return user.getIdUser();
    }

    public void setIdUser(Long idUser) {
        this.user.setIdUser(idUser);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "idNotification=" + idNotification +
                ", idUser=" + user.getIdUser() +
                ", content='" + content + '\'' +
                ", read=" + read +
                ", date=" + date +
                '}';
    }

}
