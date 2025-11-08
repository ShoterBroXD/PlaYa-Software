package com.playa.unit;

import com.playa.dto.UserRequestDto;
import com.playa.dto.UserResponseDto;
import com.playa.model.enums.Rol;
import com.playa.mapper.UserMapper;
import com.playa.model.User;
import com.playa.repository.UserRepository;
import com.playa.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User artist1;
    private User artist2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        artist1 = User.builder()
                .idUser(1L)
                .name("Artist One")
                .email("artist1@mail.com")
                .type(Rol.ARTIST)
                .registerDate(LocalDateTime.now().minusDays(3))
                .premium(false)
                .active(true)
                .build();

        artist2 = User.builder()
                .idUser(2L)
                .name("Artist Two")
                .email("artist2@mail.com")
                .type(Rol.ARTIST)
                .registerDate(LocalDateTime.now().minusDays(7))
                .premium(true)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("CP-035: Mostrar nuevos artistas")
    void getNewArtists_ShouldReturnRecentArtists() {
        // Arrange
        List<User> mockArtists = Arrays.asList(artist1, artist2);

        List<UserResponseDto> mockResponse = mockArtists.stream()
                .map(a -> UserResponseDto.builder()
                        .idUser(a.getIdUser())
                        .name(a.getName())
                        .email(a.getEmail())
                        .type(a.getType())
                        .build())
                .collect(Collectors.toList());

        when(userRepository.findNewArtists(any(LocalDateTime.class))).thenReturn(mockArtists);
        when(userMapper.convertToResponseDto(artist1)).thenReturn(mockResponse.get(0));
        when(userMapper.convertToResponseDto(artist2)).thenReturn(mockResponse.get(1));

        // Act
        List<UserResponseDto> response = userService.getNewArtists(); // usa tu metodo de service

        // Assert
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getName()).isEqualTo("Artist One");
        assertThat(response.get(1).getEmail()).isEqualTo("artist2@mail.com");

        verify(userRepository, times(1)).findNewArtists(any(LocalDateTime.class));
        verify(userMapper, times(2)).convertToResponseDto(any(User.class));
    }

    @Test
    @DisplayName("CP-036: No hay nuevos artistas para mostrar")
    void getNewArtists_ShouldReturnEmptyList_WhenNoNewArtists() {
        // Arrange
        when(userRepository.findNewArtists(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // Act
        List<UserResponseDto> response = userService.getNewArtists();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();

        verify(userRepository, times(1)).findNewArtists(any(LocalDateTime.class));
        verifyNoInteractions(userMapper);
    }

}
