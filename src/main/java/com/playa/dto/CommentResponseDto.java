package com.playa.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentResponseDto {
    // Constructores, getters y setters
    private Long idComment;
    private Long idUser;
    private Long idSong;
    private String content;
    private Long parentComment;
    private LocalDateTime date;
}