package com.playa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistSongId implements Serializable {

    @Column(name = "idplaylist")
    private Long idPlaylist;

    @Column(name = "idsong")
    private Long idSong;
}
