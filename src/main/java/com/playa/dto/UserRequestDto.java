package com.playa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class UserRequestDto {

    @NonNull
    @Size(max = 100)
    private String name;
    @Email
    private String email;
    private String password;
    private String type;
    private String biography;
    private String redSocial;
}
