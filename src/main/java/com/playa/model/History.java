package com.playa.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "songs_history")
@Builder
@Data
@Getter
@Setter
public class History {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idUser", nullable = false)
    private Long idUser;

    @Column(name = "idSong", nullable = false)
    private Long idSong;

    @Column(nullable = false, name = "dateplayed")
    private LocalDateTime datePlayed;

    // Constructores
    public History() {}
    public History(Long user, Long song, LocalDateTime datePlayed) {
        this.idUser = user;
        this.idSong = song;
        this.datePlayed = datePlayed;
    }

}

