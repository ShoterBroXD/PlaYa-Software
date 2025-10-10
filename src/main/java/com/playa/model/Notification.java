package com.playa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idnotification")
    private Long idNotification;

    @Column(nullable = false, name = "iduser")
    private Long idUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, name = "read")
    private Boolean read;

    @Column(nullable = false)
    private LocalDateTime date;
}
