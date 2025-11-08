package com.playa.unit;

import com.playa.dto.SongRequestDto;
import com.playa.dto.SongResponseDto;
import com.playa.model.Genre;
import com.playa.model.Song;
import com.playa.model.User;
import com.playa.model.enums.Rol;
import com.playa.repository.GenreRepository;
import com.playa.repository.SongRepository;
import com.playa.repository.UserRepository;
import com.playa.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private SongService songService;

    private User basicArtist;
    private SongRequestDto songRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        basicArtist = User.builder()
                .idUser(1L)
                .email("artist@mail.com")
                .name("Basic Artist")
                .type(Rol.ARTIST)
                .premium(false)
                .active(true)
                .registerDate(LocalDateTime.now())
                .build();

        songRequest = new SongRequestDto();
        songRequest.setTitle("New Song");
        songRequest.setDescription("Test description");
        songRequest.setCoverURL("https://cdn.playa.com/cover1.jpg");
        songRequest.setFileURL("https://cdn.playa.com/audio1.mp3");
        songRequest.setVisibility("public");
        songRequest.setIdgenre(1L);
    }

    @Test
    @DisplayName("CP-039: Subida gratis de canción")
    void createSong_BasicUserUnderLimit_ShouldUploadSuccessfully() {
        // Arrange
        User user = User.builder()
                .idUser(1L)
                .name("Basic Artist")
                .premium(false)
                .type(Rol.ARTIST)
                .build();

        SongRequestDto songRequest = SongRequestDto.builder()
                .title("Test Song")
                .idgenre(1L)
                .fileURL("test.mp3")
                .coverURL("cover.jpg") // ✅ Añadido para pasar la validación
                .build();

        Genre genre = Genre.builder()
                .idGenre(1L)
                .name("Rock")
                .build();

        Song song = Song.builder()
                .idSong(1L)
                .title("Test Song")
                .user(user)
                .genre(genre)
                .uploadDate(LocalDateTime.now())
                .build();

        // ✅ Mockeamos repositorios
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(songRepository.countByUserAndVisibilityNot(user, "deleted")).thenReturn(5L);
        when(songRepository.save(any(Song.class))).thenReturn(song);

        // Act
        SongResponseDto result = songService.createSong(1L, songRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Song");
        assertThat(result.getIdUser()).isEqualTo(1L);
        assertThat(result.getGenre().getName()).isEqualTo("Rock");

        verify(userRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(songRepository).save(any(Song.class));
    }
    // habian errores
    @Test
    @DisplayName("CP-040: Falta de archivo al subir cancion")
    void createSong_MissingCover_ShouldThrowException() {
        // Arrange
        User user = User.builder()
                .idUser(1L)
                .name("Basic Artist")
                .premium(false)
                .type(Rol.ARTIST)
                .build();

        SongRequestDto songRequest = SongRequestDto.builder()
                .title("Song without cover")
                .idgenre(1L)
                .fileURL("song.mp3")
                .coverURL(null) // ❌ Falta portada
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act + Assert
        assertThatThrownBy(() -> songService.createSong(1L, songRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La URL de la portada no puede estar vacía");

        // ✅ Solo se verifica la llamada a userRepository
        verify(userRepository).findById(1L);
        verifyNoInteractions(genreRepository);
        verify(songRepository, never()).save(any(Song.class));
    }
}