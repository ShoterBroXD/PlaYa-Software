package com.playa.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentRequestDto {
    // Constructores, getters y setters
    private Long idUser;
    private Long idSong;
    private String content;
    private Long parentComment; // null si no es respuesta

}
