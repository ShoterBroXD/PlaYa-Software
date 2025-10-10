package com.playa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {

    private Long idComment;
    private Long idUser;
    private Long idSong;
    private String content;
    private Long parentComment;

    // Constructores, getters y setters
    CommentResponseDto(Long idComment, Long idUser, Long idSong, String content, Long parentComment) {
        this.idComment = idComment;
        this.idUser = idUser;
        this.idSong = idSong;
        this.content = content;
        this.parentComment = parentComment;
    }

}
