package com.playa.unit;

import com.playa.dto.*;
import com.playa.exception.PlayerException;
import com.playa.exception.QueueEmptyException;
import com.playa.exception.ResourceNotFoundException;
import com.playa.model.*;
import com.playa.model.enums.Mode;
import com.playa.repository.*;
import com.playa.service.PlayerService;
import com.playa.service.SongService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlayerService - Pruebas Unitarias - US-008")
class PlayerServiceTest {

    @Mock
    private PlayerStateRepository playerStateRepository;

    @Mock
    private PlayQueueRepository playQueueRepository;

    @Mock
    private SongHistoryRepository songHistoryRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SongService songService;

    @InjectMocks
    private PlayerService playerService;

    @Test
    @DisplayName("Debe registrar reproducción correctamente")
    void registerPlay_validData_registersPlayHistory() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        Song song = new Song();
        song.setIdSong(10L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(songRepository.findById(10L)).thenReturn(Optional.of(song));
        when(songHistoryRepository.findById(any())).thenReturn(Optional.empty());
        when(songHistoryRepository.save(any(SongHistory.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        playerService.registerPlay(1L, 10L);

        // Assert
        verify(songHistoryRepository, times(1)).save(argThat(history ->
                history.getUser().equals(user) &&
                        history.getSong().equals(song) &&
                        history.getDatePlayed() != null
        ));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el usuario no existe al registrar reproducción")
    void registerPlay_userNotFound_throwsException() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> playerService.registerPlay(99L, 10L));

        verify(songHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la canción no existe al registrar reproducción")
    void registerPlay_songNotFound_throwsException() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(songRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> playerService.registerPlay(1L, 99L));

        verify(songHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe obtener la reproducción actual correctamente")
    void getCurrentPlayback_validState_returnsCurrentPlayback() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        Song song = new Song();
        song.setIdSong(10L);
        song.setTitle("Test Song");
        song.setUser(user);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setCurrentSong(song);
        state.setIsPlaying(true);
        state.setIsPaused(false);
        state.setVolume(80);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));

        // Act
        CurrentPlaybackResponseDto response = playerService.getCurrentPlayback(1L);

        // Assert
        assertNotNull(response);
        assertEquals(10L, response.getIdSong());
        assertEquals("Test Song", response.getTitle());
        assertTrue(response.getIsPlaying());
        assertFalse(response.getIsPaused());
    }

    @Test
    @DisplayName("Debe lanzar excepción si no hay reproducción activa")
    void getCurrentPlayback_noCurrentSong_throwsException() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setCurrentSong(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> playerService.getCurrentPlayback(1L));
    }

    @Test
    @DisplayName("Debe reproducir una canción correctamente")
    void playSong_validRequest_startsPlayback() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        Song song = new Song();
        song.setIdSong(10L);
        song.setTitle("Test Song");
        song.setUser(user);
        song.setVisibility("public");

        PlaySongRequestDto request = new PlaySongRequestDto();
        request.setIdSong(10L);

        PlayerState state = new PlayerState();
        state.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(songRepository.findById(10L)).thenReturn(Optional.of(song));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));
        when(playerStateRepository.save(any(PlayerState.class))).thenReturn(state);
        when(songHistoryRepository.save(any(SongHistory.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        PlaybackControlResponseDto response = playerService.playSong(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals("Reproducción iniciada", response.getMessage());
        assertTrue(response.getIsPlaying());
        assertFalse(response.getIsPaused());
        assertEquals(10L, response.getSong().getIdSong());
        verify(playerStateRepository, times(1)).save(any(PlayerState.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al reproducir canción privada de otro usuario")
    void playSong_privateSongFromOtherUser_throwsException() {
        // Arrange
        User owner = new User();
        owner.setIdUser(2L);

        User user = new User();
        user.setIdUser(1L);

        Song song = new Song();
        song.setIdSong(10L);
        song.setUser(owner);
        song.setVisibility("private");

        PlaySongRequestDto request = new PlaySongRequestDto();
        request.setIdSong(10L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(songRepository.findById(10L)).thenReturn(Optional.of(song));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> playerService.playSong(1L, request));

        verify(playerStateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe pausar la reproducción correctamente")
    void pause_activePlayback_pausesSuccessfully() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setIsPlaying(true);
        state.setIsPaused(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));
        when(playerStateRepository.save(any(PlayerState.class))).thenReturn(state);

        // Act
        PlaybackControlResponseDto response = playerService.pause(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Reproducción pausada", response.getMessage());
        assertFalse(response.getIsPlaying());
        assertTrue(response.getIsPaused());
        verify(playerStateRepository, times(1)).save(state);
    }

    @Test
    @DisplayName("Debe lanzar excepción al pausar sin reproducción activa")
    void pause_noActivePlayback_throwsException() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setIsPlaying(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));

        // Act & Assert
        assertThrows(PlayerException.class,
                () -> playerService.pause(1L));

        verify(playerStateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe reanudar la reproducción correctamente")
    void resume_pausedPlayback_resumesSuccessfully() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setIsPlaying(false);
        state.setIsPaused(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));
        when(playerStateRepository.save(any(PlayerState.class))).thenReturn(state);

        // Act
        PlaybackControlResponseDto response = playerService.resume(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Reproducción reanudada", response.getMessage());
        assertTrue(response.getIsPlaying());
        assertFalse(response.getIsPaused());
        verify(playerStateRepository, times(1)).save(state);
    }

    @Test
    @DisplayName("Debe detener la reproducción correctamente")
    void stop_activePlayback_stopsSuccessfully() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        Song song = new Song();
        song.setIdSong(10L);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setCurrentSong(song);
        state.setIsPlaying(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));
        when(playerStateRepository.save(any(PlayerState.class))).thenReturn(state);

        // Act
        PlaybackControlResponseDto response = playerService.stop(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Reproducción detenida", response.getMessage());
        assertFalse(response.getIsPlaying());
        assertFalse(response.getIsPaused());
        assertNull(state.getCurrentSong());
        verify(playerStateRepository, times(1)).save(state);
    }

    @Test
    @DisplayName("Debe avanzar a la siguiente canción correctamente")
    void next_withQueue_playsNextSong() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        Song song1 = new Song();
        song1.setIdSong(10L);
        song1.setUser(user);

        Song song2 = new Song();
        song2.setIdSong(20L);
        song2.setTitle("Next Song");
        song2.setUser(user);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setCurrentSong(song1);
        state.setRepeatMode(Mode.NONE);

        PlayQueue queue1 = new PlayQueue();
        queue1.setSong(song1);
        queue1.setPosition(1);

        PlayQueue queue2 = new PlayQueue();
        queue2.setSong(song2);
        queue2.setPosition(2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));
        when(playQueueRepository.findByUserOrderByPositionAsc(user))
                .thenReturn(Arrays.asList(queue1, queue2));
        when(playerStateRepository.save(any(PlayerState.class))).thenReturn(state);
        when(songHistoryRepository.save(any(SongHistory.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        PlaybackControlResponseDto response = playerService.next(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Siguiente canción", response.getMessage());
        assertEquals(20L, response.getSong().getIdSong());
        verify(playerStateRepository, times(1)).save(state);
    }

    @Test
    @DisplayName("Debe lanzar excepción si no hay siguiente canción")
    void next_emptyQueue_throwsException() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setRepeatMode(Mode.NONE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));
        when(playQueueRepository.findByUserOrderByPositionAsc(user)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(QueueEmptyException.class,
                () -> playerService.next(1L));
    }

    @Test
    @DisplayName("Debe agregar canción a la cola correctamente")
    void addToQueue_validSong_addsToQueue() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        Song song = new Song();
        song.setIdSong(10L);
        song.setTitle("Test Song");
        song.setUser(user);

        AddToQueueRequestDto request = new AddToQueueRequestDto();
        request.setIdSong(10L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(songRepository.findById(10L)).thenReturn(Optional.of(song));
        when(playQueueRepository.countByUser(user)).thenReturn(5L);
        when(playQueueRepository.save(any(PlayQueue.class))).thenAnswer(inv -> {
            PlayQueue pq = inv.getArgument(0);
            pq.setIdQueue(1L);
            return pq;
        });

        // Act
        Map<String, Object> response = playerService.addToQueue(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals("Canción agregada a la cola", response.get("message"));
        verify(playQueueRepository, times(1)).save(any(PlayQueue.class));
    }

    @Test
    @DisplayName("Debe activar modo aleatorio correctamente")
    void setShuffle_enableShuffle_shufflesQueue() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        Song song1 = new Song();
        song1.setIdSong(10L);

        Song song2 = new Song();
        song2.setIdSong(20L);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setCurrentSong(song1);

        PlayQueue queue1 = new PlayQueue();
        queue1.setSong(song1);
        queue1.setPosition(1);

        PlayQueue queue2 = new PlayQueue();
        queue2.setSong(song2);
        queue2.setPosition(2);

        ShuffleRequestDto request = new ShuffleRequestDto();
        request.setEnabled(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));
        when(playerStateRepository.save(any(PlayerState.class))).thenReturn(state);
        when(playQueueRepository.findByUserOrderByPositionAsc(user))
                .thenReturn(Arrays.asList(queue1, queue2));
        when(playQueueRepository.save(any(PlayQueue.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Map<String, Object> response = playerService.setShuffle(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals("Modo aleatorio activado", response.get("message"));
        assertEquals(true, response.get("shuffleEnabled"));
        verify(playerStateRepository, times(1)).save(state);
    }

    @Test
    @DisplayName("Debe establecer modo de repetición correctamente")
    void setRepeatMode_validMode_setsRepeatMode() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setRepeatMode(Mode.NONE);

        RepeatModeRequestDto request = new RepeatModeRequestDto();
        request.setMode("ALL");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));
        when(playerStateRepository.save(any(PlayerState.class))).thenReturn(state);

        // Act
        Map<String, Object> response = playerService.setRepeatMode(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals("Modo repetir: toda la cola", response.get("message"));
        assertEquals("all", response.get("repeatMode"));
        assertEquals(Mode.ALL, state.getRepeatMode());
        verify(playerStateRepository, times(1)).save(state);
    }

    @Test
    @DisplayName("Debe ajustar el volumen correctamente")
    void setVolume_validVolume_setsVolume() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setVolume(50);

        VolumeRequestDto request = new VolumeRequestDto();
        request.setVolume(80);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));
        when(playerStateRepository.save(any(PlayerState.class))).thenReturn(state);

        // Act
        Map<String, Object> response = playerService.setVolume(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals("Volumen ajustado", response.get("message"));
        assertEquals(80, response.get("volume"));
        assertEquals(80, state.getVolume());
        verify(playerStateRepository, times(1)).save(state);
    }

    @Test
    @DisplayName("Debe obtener la cola de reproducción correctamente")
    void getQueue_withSongs_returnsQueue() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        Song song1 = new Song();
        song1.setIdSong(10L);
        song1.setTitle("Song 1");
        song1.setUser(user);

        Song song2 = new Song();
        song2.setIdSong(20L);
        song2.setTitle("Song 2");
        song2.setUser(user);

        PlayQueue queue1 = new PlayQueue();
        queue1.setSong(song1);
        queue1.setPosition(1);

        PlayQueue queue2 = new PlayQueue();
        queue2.setSong(song2);
        queue2.setPosition(2);

        PlayerState state = new PlayerState();
        state.setUser(user);
        state.setCurrentSong(song1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(playerStateRepository.findByUser(user)).thenReturn(Optional.of(state));
        when(playQueueRepository.findByUserOrderByPositionAsc(user))
                .thenReturn(Arrays.asList(queue1, queue2));

        // Act
        QueueResponseDto response = playerService.getQueue(1L);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getCurrentIndex());
        assertEquals(2, response.getTotalSongs());
        assertEquals(2, response.getSongs().size());
    }

    @Test
    @DisplayName("Debe eliminar canción de la cola correctamente")
    void removeFromQueue_validPosition_removesFromQueue() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(playQueueRepository).deleteByUserAndPosition(user, 2);

        // Act
        playerService.removeFromQueue(1L, 2);

        // Assert
        verify(playQueueRepository, times(1)).deleteByUserAndPosition(user, 2);
    }
}
