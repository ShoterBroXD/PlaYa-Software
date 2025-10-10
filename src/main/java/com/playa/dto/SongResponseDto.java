package com.playa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponseDto {

    private Long idSong;
    private Long idUser;
    private String title;
    private String description;
    private String coverURL;
    private String fileURL;
    private String visibility;
    private LocalDateTime uploadDate;
}
