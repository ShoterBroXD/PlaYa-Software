package com.playa.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class UserResponseDto {
    private Long idUser;
    private LocalDateTime registerDate;
    private String name;
    private String email;
    private String type;
    private String biography;
    private String redSocial;
}
