package com.playa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long idUser;
    private Long idSong;
    private String content;
    private Long parentComment; // null si no es respuesta

    // Constructores, getters y setters
    public CommentRequestDto(long idUser, String title, String description, Long idSong, Long parentComment) {
        this.idUser = idUser;
        this.idSong = idSong;
        this.content = description;
        this.parentComment = parentComment;
    }
}
