package com.playa.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="notification_preference")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpreference")
    private Long idpreference;

    @OneToOne
    @JoinColumn(name = "idUser", nullable = false, unique = true)
    private User user;

    @Column(name = "enableComments", nullable = false)
    private Boolean enableComments = true;

    @Column(name = "enableFollowers", nullable = false)
    private Boolean enableFollowers = true;

    @Column(name = "enableSistem", nullable = false)
    private Boolean enableSistem = true;

    @Column(name = "enableNewRelease", nullable = false)
    private Boolean enableNewRelease = true;
}
