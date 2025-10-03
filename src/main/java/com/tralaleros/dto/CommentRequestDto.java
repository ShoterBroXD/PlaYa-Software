package com.tralaleros.dto;

public class CommentRequestDto {
    private Long idUser;
    private Long idSong;
    private String content;
    private Long parentComment; // null si no es respuesta

    // Constructores, getters y setters
}
