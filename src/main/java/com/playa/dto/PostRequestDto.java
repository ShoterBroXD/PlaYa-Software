package com.playa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    private Long idPost;
    private Long idThread;
    @NotBlank
    @Length(max = 144)
    private String content;
}
