package com.playa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class CommentRequestDto {
    // Constructores, getters y setters
    private Long idUser;
    private Long idSong;
    @NotBlank
    @Length(max = 100)
    private String content;
    private Long parentComment; // null si no es respuesta

}