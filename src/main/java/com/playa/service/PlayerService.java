package com.playa.service;

import com.playa.dto.*;
import com.playa.dto.SongBasicResponseDto;
import com.playa.exception.PlayerException;
import com.playa.exception.QueueEmptyException;
import com.playa.exception.ResourceNotFoundException;
import com.playa.model.*;
import com.playa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerStateRepository playerStateRepository;
    private final PlayQueueRepository playQueueRepository;
    private final SongHistoryRepository songHistoryRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final SongService songService;

    @Transactional
    public void registerPlay(Long userId, Long songId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada"));

        // Crear registro de historial
        SongHistoryId historyId = new SongHistoryId(userId, songId);
        Optional<SongHistory> existingHistory = songHistoryRepository.findById(historyId);

        SongHistory history = existingHistory.orElseGet(() -> {
            SongHistory h = new SongHistory();
            h.setId(historyId);
            h.setUser(user);
            h.setSong(song);
            return h;
        });

        history.setDatePlayed(java.time.LocalDateTime.now());
        songHistoryRepository.save(history);
    }

    public CurrentPlaybackResponseDto getCurrentPlayback(Long userId) {
        User user = getUserOrThrow(userId);
        PlayerState state = playerStateRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No hay ninguna canción en reproducción"));

        if (state.getCurrentSong() == null) {
            throw new ResourceNotFoundException("No hay ninguna canción en reproducción");
        }

        return mapToCurrentPlaybackResponseDto(state);
    }

    @Transactional
    public PlaybackControlResponseDto playSong(Long userId, PlaySongRequestDto request) {
        User user = getUserOrThrow(userId);
        Song song = songRepository.findById(request.getIdSong())
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada"));

        // Validar visibilidad (RB-008)
        if ("private".equals(song.getVisibility()) && !song.getUser().getIdUser().equals(userId)) {
            throw new IllegalArgumentException("Esta canción es privada y no puedes acceder a ella");
        }

        // Obtener o crear estado del reproductor
        PlayerState state = playerStateRepository.findByUser(user)
                .orElse(new PlayerState());

        if (state.getUser() == null) {
            state.setUser(user);
        }

        // RB-008: Solo una canción puede reproducirse a la vez
        state.setCurrentSong(song);
        state.setIsPlaying(true);
        state.setIsPaused(false);
        state.setPlaybackTime(0);

        playerStateRepository.save(state);

        // RB-021: Registrar en historial
        registerPlayHistory(user, song);

        PlaybackControlResponseDto response = new PlaybackControlResponseDto();
        response.setMessage("Reproducción iniciada");
        response.setIsPlaying(true);
        response.setIsPaused(false);
        response.setSong(mapSongToResponseDto(song));

        return response;
    }

    @Transactional
    public List<SongResponseDto> getPlayHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<SongHistory> historyList = songHistoryRepository.findByUserOrderByDatePlayedDesc(user);

        if (historyList.isEmpty()) {
            throw new ResourceNotFoundException("El usuario no tiene historial de reproducción");
        }

        return historyList.stream()
                .map(history -> songService.getSongById(history.getSong().getIdSong()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlaybackControlResponseDto pause(Long userId) {
        PlayerState state = getPlayerStateOrThrow(userId);

        if (!state.getIsPlaying()) {
            throw new PlayerException("No hay reproducción activa para pausar");
        }

        state.setIsPlaying(false);
        state.setIsPaused(true);
        playerStateRepository.save(state);

        return new PlaybackControlResponseDto("Reproducción pausada", false, true, null);
    }

    @Transactional
    public PlaybackControlResponseDto resume(Long userId) {
        PlayerState state = getPlayerStateOrThrow(userId);

        if (!state.getIsPaused()) {
            throw new PlayerException("No hay reproducción pausada para reanudar");
        }

        state.setIsPlaying(true);
        state.setIsPaused(false);
        playerStateRepository.save(state);

        return new PlaybackControlResponseDto("Reproducción reanudada", true, false, null);
    }

    @Transactional
    public PlaybackControlResponseDto stop(Long userId) {
        PlayerState state = getPlayerStateOrThrow(userId);

        state.setCurrentSong(null);
        state.setIsPlaying(false);
        state.setIsPaused(false);
        state.setPlaybackTime(0);
        playerStateRepository.save(state);

        return new PlaybackControlResponseDto("Reproducción detenida", false, false, null);
    }

    @Transactional
    public PlaybackControlResponseDto next(Long userId) {
        User user = getUserOrThrow(userId);
        PlayerState state = getPlayerStateOrThrow(userId);

        // Buscar siguiente en la cola
        List<PlayQueue> queue = playQueueRepository.findByUserOrderByPositionAsc(user);

        if (queue.isEmpty()) {
            throw new QueueEmptyException("No hay siguiente canción en la cola");
        }

        // Encontrar índice actual
        int currentIndex = -1;
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getSong().getIdSong().equals(state.getCurrentSong().getIdSong())) {
                currentIndex = i;
                break;
            }
        }

        // Siguiente canción
        int nextIndex = currentIndex + 1;
        if (nextIndex >= queue.size()) {
            // Si estamos en repeat all, volver al inicio
            if ("all".equals(state.getRepeatMode())) {
                nextIndex = 0;
            } else {
                throw new QueueEmptyException("No hay más canciones en la cola");
            }
        }

        Song nextSong = queue.get(nextIndex).getSong();
        state.setCurrentSong(nextSong);
        state.setPlaybackTime(0);
        playerStateRepository.save(state);

        // Registrar en historial
        registerPlayHistory(user, nextSong);

        PlaybackControlResponseDto response = new PlaybackControlResponseDto();
        response.setMessage("Siguiente canción");
        response.setIsPlaying(true);
        response.setIsPaused(false);
        response.setSong(mapSongToResponseDto(nextSong));

        return response;
    }

    @Transactional
    public PlaybackControlResponseDto previous(Long userId) {
        User user = getUserOrThrow(userId);
        PlayerState state = getPlayerStateOrThrow(userId);

        List<PlayQueue> queue = playQueueRepository.findByUserOrderByPositionAsc(user);

        if (queue.isEmpty()) {
            throw new QueueEmptyException("No hay canción anterior en la cola");
        }

        int currentIndex = -1;
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getSong().getIdSong().equals(state.getCurrentSong().getIdSong())) {
                currentIndex = i;
                break;
            }
        }

        int previousIndex = currentIndex - 1;
        if (previousIndex < 0) {
            if ("all".equals(state.getRepeatMode())) {
                previousIndex = queue.size() - 1;
            } else {
                throw new QueueEmptyException("No hay canción anterior en la cola");
            }
        }

        Song previousSong = queue.get(previousIndex).getSong();
        state.setCurrentSong(previousSong);
        state.setPlaybackTime(0);
        playerStateRepository.save(state);

        registerPlayHistory(user, previousSong);

        PlaybackControlResponseDto response = new PlaybackControlResponseDto();
        response.setMessage("Canción anterior");
        response.setIsPlaying(true);
        response.setIsPaused(false);
        response.setSong(mapSongToResponseDto(previousSong));

        return response;
    }

    @Transactional
    public Map<String, Object> setShuffle(Long userId, ShuffleRequestDto request) {
        PlayerState state = getPlayerStateOrThrow(userId);

        state.setShuffleEnabled(request.getEnabled());
        playerStateRepository.save(state);

        Map<String, Object> response = new HashMap<>();
        response.put("message", request.getEnabled() ? "Modo aleatorio activado" : "Modo aleatorio desactivado");
        response.put("shuffleEnabled", request.getEnabled());

        return response;
    }

    @Transactional
    public Map<String, Object> setRepeatMode(Long userId, RepeatModeRequestDto request) {
        PlayerState state = getPlayerStateOrThrow(userId);

        state.setRepeatMode(request.getMode());
        playerStateRepository.save(state);

        String modeDescription = switch (request.getMode()) {
            case "none" -> "sin repetición";
            case "one" -> "una canción";
            case "all" -> "toda la cola";
            default -> request.getMode();
        };

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Modo repetir: " + modeDescription);
        response.put("repeatMode", request.getMode());

        return response;
    }

    @Transactional
    public Map<String, Object> setVolume(Long userId, VolumeRequestDto request) {
        PlayerState state = getPlayerStateOrThrow(userId);

        state.setVolume(request.getVolume());
        playerStateRepository.save(state);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Volumen ajustado");
        response.put("volume", request.getVolume());

        return response;
    }

    public QueueResponseDto getQueue(Long userId) {
        User user = getUserOrThrow(userId);
        PlayerState state = playerStateRepository.findByUser(user).orElse(null);

        List<PlayQueue> queue = playQueueRepository.findByUserOrderByPositionAsc(user);

        int currentIndex = -1;
        if (state != null && state.getCurrentSong() != null) {
            for (int i = 0; i < queue.size(); i++) {
                if (queue.get(i).getSong().getIdSong().equals(state.getCurrentSong().getIdSong())) {
                    currentIndex = i;
                    break;
                }
            }
        }

        List<QueueSongResponseDto> songs = queue.stream()
                .map(this::mapToQueueSongResponseDto)
                .collect(Collectors.toList());

        QueueResponseDto response = new QueueResponseDto();
        response.setCurrentIndex(currentIndex);
        response.setTotalSongs(songs.size());
        response.setSongs(songs);

        return response;
    }

    @Transactional
    public Map<String, Object> addToQueue(Long userId, AddToQueueRequestDto request) {
        User user = getUserOrThrow(userId);
        Song song = songRepository.findById(request.getIdSong())
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada"));

        // Obtener siguiente posición en la cola
        Long count = playQueueRepository.countByUser(user);
        Integer nextPosition = count.intValue() + 1;

        PlayQueue queueItem = new PlayQueue();
        queueItem.setUser(user);
        queueItem.setSong(song);
        queueItem.setPosition(nextPosition);

        playQueueRepository.save(queueItem);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Canción agregada a la cola");
        response.put("song", mapSongToBasicResponseDto(song));
        response.put("queuePosition", nextPosition);

        return response;
    }

    @Transactional
    public void removeFromQueue(Long userId, Integer position) {
        User user = getUserOrThrow(userId);
        playQueueRepository.deleteByUserAndPosition(user, position);
    }

    public List<HistoryResponseDto> getHistory(Long userId, Integer limit) {
        User user = getUserOrThrow(userId);

        List<SongHistory> history = songHistoryRepository.findByUserOrderByDatePlayedDesc(user);

        return history.stream()
                .limit(limit != null ? limit : 50)
                .map(h -> new HistoryResponseDto(mapSongToResponseDto(h.getSong()), h.getDatePlayed()))
                .collect(Collectors.toList());
    }

    public List<RecommendationResponseDto> getRecommendations(Long userId) {
        User user = getUserOrThrow(userId);

        // Obtener géneros más escuchados del historial
        List<SongHistory> history = songHistoryRepository.findByUserOrderByDatePlayedDesc(user);

        if (history.isEmpty()) {
            throw new ResourceNotFoundException("No hay suficiente historial para generar recomendaciones");
        }

        // Contar géneros más escuchados
        Map<Genre, Long> genreCounts = new HashMap<>();
        for (SongHistory h : history) {
            for (Genre genre : h.getSong().getGenres()) {
                genreCounts.put(genre, genreCounts.getOrDefault(genre, 0L) + 1);
            }
        }

        // Ordenar por más escuchados
        List<Genre> topGenres = genreCounts.entrySet().stream()
                .sorted(Map.Entry.<Genre, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Buscar canciones de esos géneros que NO estén en el historial
        Set<Long> playedSongIds = history.stream()
                .map(h -> h.getSong().getIdSong())
                .collect(Collectors.toSet());

        List<RecommendationResponseDto> recommendations = new ArrayList<>();

        for (Genre genre : topGenres) {

            List<Song> songs = songRepository.findByGenres_IdGenreAndVisibility(genre.getIdGenre(), "public");

            songs.stream()
                    .filter(s -> !playedSongIds.contains(s.getIdSong()))
                    .limit(5)
                    .forEach(song -> {
                        RecommendationResponseDto rec = new RecommendationResponseDto();
                        rec.setIdSong(song.getIdSong());
                        rec.setTitle(song.getTitle());
                        rec.setArtist(mapUserToArtistResponseDto(song.getUser()));
                        rec.setCoverURL(song.getCoverURL());
                        rec.setGenres(song.getGenres().stream()
                                .map(g -> new GenreResponseDto(g.getIdGenre(), g.getName()))
                                .collect(Collectors.toSet()));
                        rec.setReason("Basado en tu historial de " + genre.getName());

                        recommendations.add(rec);
                    });
        }

        return recommendations.stream().limit(10).collect(Collectors.toList());
    }


    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private PlayerState getPlayerStateOrThrow(Long userId) {
        User user = getUserOrThrow(userId);
        return playerStateRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No hay estado de reproductor activo"));
    }

    @Transactional
    protected void registerPlayHistory(User user, Song song) {
        SongHistoryId historyId = new SongHistoryId(user.getIdUser(), song.getIdSong());

        SongHistory history = new SongHistory();
        history.setId(historyId);
        history.setUser(user);
        history.setSong(song);
        history.setDatePlayed(LocalDateTime.now());

        songHistoryRepository.save(history);
    }

    private CurrentPlaybackResponseDto mapToCurrentPlaybackResponseDto(PlayerState state) {
        Song song = state.getCurrentSong();

        CurrentPlaybackResponseDto response = new CurrentPlaybackResponseDto();
        response.setIdSong(song.getIdSong());
        response.setTitle(song.getTitle());
        response.setArtist(mapUserToArtistResponseDto(song.getUser()));
        response.setCoverURL(song.getCoverURL());
        response.setFileURL(song.getFileURL());
        response.setIsPlaying(state.getIsPlaying());
        response.setIsPaused(state.getIsPaused());
        response.setCurrentTime(state.getPlaybackTime());
        response.setDuration(null); // Calcular desde metadata del archivo o BD
        response.setVolume(state.getVolume());
        response.setShuffleEnabled(state.getShuffleEnabled());
        response.setRepeatMode(state.getRepeatMode());

        return response;
    }

    private SongResponseDto mapSongToResponseDto(Song song) {
        SongResponseDto response = new SongResponseDto();
        response.setIdSong(song.getIdSong());
        response.setTitle(song.getTitle());
        response.setDescription(song.getDescription());
        response.setCoverURL(song.getCoverURL());
        response.setFileURL(song.getFileURL());
        response.setVisibility(song.getVisibility());
        response.setUploadDate(song.getUploadDate());

        // Mapear artista
        response.setArtist(mapUserToArtistResponseDto(song.getUser()));

        // Mapear géneros
        Set<GenreResponseDto> genres = song.getGenres().stream()
                .map(g -> new GenreResponseDto(g.getIdGenre(), g.getName()))
                .collect(Collectors.toSet());
        response.setGenres(genres);

        return response;
    }

    private SongBasicResponseDto mapSongToBasicResponseDto(Song song) {
        SongBasicResponseDto response = new SongBasicResponseDto();
        response.setIdSong(song.getIdSong());
        response.setTitle(song.getTitle());
        response.setArtistName(song.getUser().getName());
        response.setCoverURL(song.getCoverURL());
        response.setDuration(song.getDuration());
        return response;
    }

    private ArtistResponseDto mapUserToArtistResponseDto(User user) {
        ArtistResponseDto response = new ArtistResponseDto();
        response.setIdUser(user.getIdUser());
        response.setName(user.getName());
        response.setBiography(user.getBiography());
        return response;
    }

    private QueueSongResponseDto mapToQueueSongResponseDto(PlayQueue queue) {
        Song song = queue.getSong();

        QueueSongResponseDto response = new QueueSongResponseDto();
        response.setPosition(queue.getPosition());
        response.setIdSong(song.getIdSong());
        response.setTitle(song.getTitle());
        response.setArtist(mapUserToArtistResponseDto(song.getUser()));
        response.setCoverURL(song.getCoverURL());
        response.setDuration(null); // Agregar si tienes en BD

        return response;
    }

}