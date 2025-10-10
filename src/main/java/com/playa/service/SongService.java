package com.playa.service;

import com.playa.dto.SongRequestDto;
import com.playa.dto.SongResponseDto;
import com.playa.exception.ResourceNotFoundException;
import com.playa.model.Song;
import com.playa.repository.SongRepository;
import com.playa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final UserRepository userRepository;

    @Transactional
    public SongResponseDto createSong(SongRequestDto requestDto) {
        // Validar que el usuario existe
        if (!userRepository.existsById(requestDto.getIdUser())) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + requestDto.getIdUser());
        }

        Song song = Song.builder()
                .idUser(requestDto.getIdUser())
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .coverURL(requestDto.getCoverURL())
                .fileURL(requestDto.getFileURL())
                .visibility(requestDto.getVisibility())
                .uploadDate(LocalDateTime.now())
                .build();

        Song savedSong = songRepository.save(song);
        return convertToResponseDto(savedSong);
    }

    @Transactional(readOnly = true)
    public SongResponseDto getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con ID: " + id));
        return convertToResponseDto(song);
    }

    @Transactional
    public SongResponseDto updateSong(Long id, SongRequestDto requestDto) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con ID: " + id));

        // Actualizar solo los campos que no son null
        if (requestDto.getTitle() != null) {
            song.setTitle(requestDto.getTitle());
        }
        if (requestDto.getDescription() != null) {
            song.setDescription(requestDto.getDescription());
        }
        if (requestDto.getVisibility() != null) {
            song.setVisibility(requestDto.getVisibility());
        }
        // coverURL y fileURL normalmente no se actualizan después de la creación

        Song updatedSong = songRepository.save(song);
        return convertToResponseDto(updatedSong);
    }

    @Transactional
    public void deleteSong(Long id) {
        if (!songRepository.existsById(id)) {
            throw new ResourceNotFoundException("Canción no encontrada con ID: " + id);
        }
        songRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SongResponseDto> getSongsByUser(Long userId) {
        List<Song> songs = songRepository.findByIdUserOrderByUploadDateDesc(userId);
        return songs.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SongResponseDto> getPublicSongs() {
        List<Song> songs = songRepository.findPublicSongsOrderByUploadDateDesc();
        return songs.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private SongResponseDto convertToResponseDto(Song song) {
        return SongResponseDto.builder()
                .idSong(song.getIdSong())
                .idUser(song.getIdUser())
                .title(song.getTitle())
                .description(song.getDescription())
                .coverURL(song.getCoverURL())
                .fileURL(song.getFileURL())
                .visibility(song.getVisibility())
                .uploadDate(song.getUploadDate())
                .build();
    }
}
