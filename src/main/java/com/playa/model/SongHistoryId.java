package com.playa.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongHistoryId implements java.io.Serializable {
    private Long idSong;
    private Long idUser;
}
