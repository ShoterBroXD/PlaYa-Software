package com.playa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Entity
@Table(name = "player_state")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_player_state")
    private Long idPlayerState;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser", nullable = false, unique = true) // Un usuario = un estado de reproductor
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idsong")
    private Song currentSong; // Canción actual (puede ser null si no hay reproducción)

    @Column(name = "is_playing", nullable = false)
    private Boolean isPlaying = false;

    @Column(name = "is_paused", nullable = false)
    private Boolean isPaused = false;

    @Column(name = "playback_time")
    private Integer playbackTime = 0; // Segundos de reproducción actual

    @Column(name = "volume")
    private Integer volume = 80; // Volumen (0-100)

    @Column(name = "shuffle_enabled", nullable = false)
    private Boolean shuffleEnabled = false;

    @Column(name = "repeat_mode", length = 10)
    private String repeatMode = "none"; // none, one, all

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
