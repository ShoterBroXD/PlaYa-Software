package com.playa.service;

import com.playa.dto.SongResponseDto;
import com.playa.exception.ResourceNotFoundException;
import com.playa.model.*;
import com.playa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {

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
        SongHistory history = new SongHistory();
        history.setId(historyId);
        history.setUser(user);
        history.setSong(song);

        songHistoryRepository.save(history);
    }

    public List<SongResponseDto> getPlayHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return songHistoryRepository.findByUserOrderByDatePlayedDesc(user).stream()
                .map(history -> songService.getSongById(history.getSong().getIdSong()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}