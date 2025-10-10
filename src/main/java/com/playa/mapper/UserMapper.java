package com.playa.mapper;

import com.playa.dto.UserRequestDto;
import com.playa.dto.UserResponseDto;
import com.playa.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User convertToEntity(UserRequestDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .type(dto.getType())
                .biography(dto.getBiography())
                .redSocial(dto.getRedSocial())
                .build();
    }

    public UserResponseDto convertToResponseDto(User user) {
        return UserResponseDto.builder()
                .idUser(user.getIdUser())
                .name(user.getName())
                .email(user.getEmail())
                .type(user.getType())
                .registerDate(user.getRegisterDate())
                .biography(user.getBiography())
                .redSocial(user.getRedSocial())
                .build();
    }
}
