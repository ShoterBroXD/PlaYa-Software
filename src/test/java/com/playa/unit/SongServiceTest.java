package com.playa.unit;

import com.playa.dto.SongRequestDto;
import com.playa.dto.SongResponseDto;
import com.playa.exception.ResourceNotFoundException;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para SongService - Funcionalidad 01: Subir Canción")
class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private SongService songService;

    private User artistUser;
    private User listenerUser;
    private Genre genre;
    private SongRequestDto songRequestDto;
    private Song song;

    @BeforeEach
    void setUp() {
        // Usuario artista
        artistUser = User.builder()
                .idUser(1L)
                .name("Test Artist")
                .email("artist@test.com")
                .type(Rol.ARTIST)
                .premium(false)
                .registerDate(LocalDateTime.now())
                .build();

        // Usuario listener
        listenerUser = User.builder()
                .idUser(2L)
                .name("Test Listener")
                .email("listener@test.com")
                .type(Rol.LISTENER)
                .premium(true)
                .registerDate(LocalDateTime.now())
                .build();

        // Género
        genre = Genre.builder()
                .idGenre(1L)
                .name("Rock")
                .build();

        // DTO de request
        songRequestDto = SongRequestDto.builder()
                .title("Test Song")
                .description("Test Description")
                .coverURL("https://example.com/cover.jpg")
                .fileURL("https://example.com/song.mp3")
                .visibility("public")
                .idgenre(1L)
                .duration(180.0f)
                .build();

        // Entidad Song
        song = Song.builder()
                .idSong(1L)
                .title("Test Song")
                .description("Test Description")
                .coverURL("https://example.com/cover.jpg")
                .fileURL("https://example.com/song.mp3")
                .visibility("public")
                .duration(180.0f)
                .user(artistUser)
                .genre(genre)
                .uploadDate(LocalDateTime.now())
                .visible(true)
                .build();
    }

    @Test
    @DisplayName("Crear canción con usuario artista válido - Debería crear exitosamente")
    void createSong_WithValidArtistUser_ShouldCreateSongSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(artistUser));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(songRepository.countByUserAndVisibilityNot(artistUser, "deleted")).thenReturn(5L);
        when(songRepository.save(any(Song.class))).thenReturn(song);

        // When
        SongResponseDto result = songService.createSong(1L, songRequestDto);

        // Then
        assertNotNull(result);
        assertEquals("Test Song", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals("public", result.getVisibility());
        assertEquals(1L, result.getIdUser());

        verify(userRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(songRepository).save(any(Song.class));
    }

    @Test
    @DisplayName("Crear canción con usuario no existente - Debería lanzar ResourceNotFoundException")
    void createSong_WithNonExistentUser_ShouldThrowResourceNotFoundException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.createSong(999L, songRequestDto);
        });

        verify(userRepository).findById(999L);
        verify(songRepository, never()).save(any(Song.class));
    }

    @Test
    @DisplayName("Usuario gratuito excede límite - Debería lanzar IllegalStateException")
    void createSong_FreeUserExceedsLimit_ShouldThrowIllegalStateException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(artistUser));
        when(songRepository.countByUserAndVisibilityNot(artistUser, "deleted")).thenReturn(10L);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            songService.createSong(1L, songRequestDto);
        });

        assertTrue(exception.getMessage().contains("no pueden subir más de"));
        verify(songRepository, never()).save(any(Song.class));
    }

    @Test
    @DisplayName("Usuario premium puede exceder límite - Debería crear canción exitosamente")
    void createSong_PremiumUserCanExceedLimit_ShouldCreateSongSuccessfully() {
        // Given
        when(userRepository.findById(2L)).thenReturn(Optional.of(listenerUser));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        // When
        SongResponseDto result = songService.createSong(2L, songRequestDto);

        // Then
        assertNotNull(result);
        verify(songRepository, never()).countByUserAndVisibilityNot(any(), any());
        verify(songRepository).save(any(Song.class));
    }

    @Test
    @DisplayName("Formato de archivo inválido - Debería lanzar IllegalArgumentException")
    void createSong_WithInvalidFileFormat_ShouldThrowIllegalArgumentException() {
        // Given
        songRequestDto.setFileURL("https://example.com/song.txt");
        when(userRepository.findById(1L)).thenReturn(Optional.of(artistUser));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            songService.createSong(1L, songRequestDto);
        });

        assertTrue(exception.getMessage().contains("Formato de archivo no permitido"));
        verify(songRepository, never()).save(any(Song.class));
    }

    @Test
    @DisplayName("Obtener canción por ID válido - Debería retornar canción")
    void getSongById_WithValidId_ShouldReturnSong() {
        // Given
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        // When
        SongResponseDto result = songService.getSongById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getIdSong());
        assertEquals("Test Song", result.getTitle());
        verify(songRepository).findById(1L);
    }

    @Test
    @DisplayName("Obtener canción por ID inválido - Debería lanzar ResourceNotFoundException")
    void getSongById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Given
        when(songRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.getSongById(999L);
        });

        verify(songRepository).findById(999L);
    }

    @Test
    @DisplayName("Actualizar canción con datos válidos - Debería actualizar exitosamente")
    void updateSong_WithValidData_ShouldUpdateSuccessfully() {
        // Given
        SongRequestDto updateDto = SongRequestDto.builder()
                .title("Updated Song")
                .description("Updated Description")
                .visibility("private")
                .build();

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        // When
        SongResponseDto result = songService.updateSong(1L, updateDto);

        // Then
        assertNotNull(result);
        verify(songRepository).findById(1L);
        verify(songRepository).save(any(Song.class));
    }

    @Test
    @DisplayName("Eliminar canción con ID válido - Debería eliminar exitosamente")
    void deleteSong_WithValidId_ShouldDeleteSuccessfully() {
        // Given
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        // When
        songService.deleteSong(1L);

        // Then
        verify(songRepository).findById(1L);
        verify(songRepository).delete(song);
    }

    @Test
    @DisplayName("Reportar canción - Debería ocultar canción")
    void reportSong_WithValidId_ShouldHideSong() {
        // Given
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        // When
        songService.reportSong(1L);

        // Then
        verify(songRepository).findById(1L);
        verify(songRepository).save(any(Song.class));
    }

    @Test
    @DisplayName("Des-reportar canción - Debería mostrar canción")
    void unreportSong_WithValidId_ShouldShowSong() {
        // Given
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        // When
        songService.unreportSong(1L);

        // Then
        verify(songRepository).findById(1L);
        verify(songRepository).save(any(Song.class));
    }
}
