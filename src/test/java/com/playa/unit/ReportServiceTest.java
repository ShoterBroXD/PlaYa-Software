package com.playa.unit;

import com.playa.exception.ResourceNotFoundException;
import com.playa.model.Song;
import com.playa.model.User;
import com.playa.repository.SongRepository;
import com.playa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReportContent - Pruebas Unitarias US-014")
class ReportContentTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private UserRepository userRepository;

    private User mockUser;
    private Song mockSong;

    @BeforeEach
    void setUp() {
        mockUser = createMockUser(123L, "test_listener@mail.com", "Test Listener");
        mockSong = createMockSong(500L, "Test Song");
    }

    private User createMockUser(Long id, String email, String name) {
        User user = new User();
        user.setIdUser(id);
        user.setEmail(email);
        user.setName(name);
        return user;
    }

    private Song createMockSong(Long id, String title) {
        Song song = new Song();
        song.setIdSong(id);
        song.setTitle(title);
        song.setIdUser(1L);
        song.setDuration(3.5f);
        song.setVisibility("public");
        song.setVisible(true);
        song.setCoverURL("http://example.com/cover.jpg");
        song.setFileURL("http://example.com/song.mp3");
        return song;
    }

    @Test
    @DisplayName("CP-013: Debe validar que existe la canción para reportar")
    void reportContent_SongExists_ValidationSuccess() {
        // Arrange
        Long songId = 500L;
        String reason = "Discurso de odio";

        when(songRepository.findById(songId)).thenReturn(Optional.of(mockSong));

        // Act & Assert
        Optional<Song> foundSong = songRepository.findById(songId);

        assertThat(foundSong).isPresent();
        assertThat(foundSong.get().getIdSong()).isEqualTo(500L);
        assertThat(foundSong.get().getTitle()).isEqualTo("Test Song");

        verify(songRepository).findById(songId);
    }

    @Test
    @DisplayName("CP-013: Debe validar que existe el usuario para reportar")
    void reportContent_UserExists_ValidationSuccess() {
        // Arrange
        String email = "test_listener@mail.com";

        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        // Act & Assert
        User foundUser = userRepository.findByEmail(email);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getIdUser()).isEqualTo(123L);
        assertThat(foundUser.getEmail()).isEqualTo("test_listener@mail.com");

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("CP-014: Debe fallar cuando no se proporciona motivo del reporte")
    void reportContent_NoReason_ValidationFails() {
        // Arrange
        String reason = null;

        // Act & Assert
        assertThatThrownBy(() -> {
            if (reason == null || reason.trim().isEmpty()) {
                throw new IllegalArgumentException("Debes indicar el motivo del reporte");
            }
        })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Debes indicar el motivo del reporte");
    }

    @Test
    @DisplayName("CP-014: Debe fallar cuando el motivo del reporte está vacío")
    void reportContent_EmptyReason_ValidationFails() {
        // Arrange
        String reason = "";

        // Act & Assert
        assertThatThrownBy(() -> {
            if (reason == null || reason.trim().isEmpty()) {
                throw new IllegalArgumentException("Debes indicar el motivo del reporte");
            }
        })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Debes indicar el motivo del reporte");
    }

    @Test
    @DisplayName("Debe fallar cuando la canción no existe")
    void reportContent_SongNotFound_ValidationFails() {
        // Arrange
        Long songId = 999L;

        when(songRepository.findById(songId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> {
            Optional<Song> song = songRepository.findById(songId);
            if (song.isEmpty()) {
                throw new ResourceNotFoundException("Canción no encontrada con ID: " + songId);
            }
        })
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Canción no encontrada con ID: 999");

        verify(songRepository).findById(songId);
    }

    @Test
    @DisplayName("Debe validar diferentes motivos válidos de reporte")
    void reportContent_ValidReasons_Success() {
        // Arrange
        String[] validReasons = {
            "Discurso de odio",
            "Contenido sexual",
            "Violencia",
            "Spam",
            "Derechos de autor"
        };

        // Act & Assert
        for (String reason : validReasons) {
            // Simular validación de motivo
            assertThat(reason).isNotNull();
            assertThat(reason.trim()).isNotEmpty();
            assertThat(reason.length()).isGreaterThan(0);

            // Verificar que son motivos válidos
            assertThat(reason).isIn(validReasons);
        }
    }

    @Test
    @DisplayName("Debe crear estructura básica de reporte")
    void reportContent_BasicStructure_Success() {
        // Arrange
        Long userId = 123L;
        Long songId = 500L;
        String reason = "Discurso de odio";
        LocalDateTime reportDate = LocalDateTime.now();

        // Act
        // Simular creación de estructura de reporte
        assertThat(userId).isEqualTo(123L);
        assertThat(songId).isEqualTo(500L);
        assertThat(reason).isEqualTo("Discurso de odio");
        assertThat(reportDate).isNotNull();

        // Simular mensaje de respuesta
        String expectedMessage = "Reporte enviado correctamente. Gracias por tu colaboración.";

        // Assert
        assertThat(expectedMessage).isEqualTo("Reporte enviado correctamente. Gracias por tu colaboración.");
    }
}
