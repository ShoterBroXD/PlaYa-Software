package com.playa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequestDto {

    private String name;
    private String email;
    private String password;
    private String type;
    private String biography;
    private String redSocial;
    private Boolean premium;
    private List<String> favoriteGenres;
}