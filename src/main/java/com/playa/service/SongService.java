package com.playa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playa.repository.SongRepository;
import com.playa.model.Song;
import com.playa.dto.SongRequestDto;
import com.playa.dto.SongResponseDto;
import com.playa.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public List<SongResponseDto> getAllSongs() {
        return songRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public SongResponseDto createSong(SongRequestDto songRequestDto) {
        Song song = new Song();
        song.setIdUser(songRequestDto.getIdUser());
        song.setTittle(songRequestDto.getTitle());
        song.setDescription(songRequestDto.getDescription());
        song.setCoverURL(songRequestDto.getCoverURL());
        song.setFileURL(songRequestDto.getFileURL());
        song.setVisibility(songRequestDto.getVisibility());
        song.setUploadDate(LocalDateTime.now());

        Song savedSong = songRepository.save(song);
        return convertToResponseDto(savedSong);
    }

    public Optional<SongResponseDto> getSongById(Long id) {
        return songRepository.findById(id)
                .map(this::convertToResponseDto);
    }

    public SongResponseDto updateSong(Long id, SongRequestDto songRequestDto) {
        Song song = songRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Canción no encontrada con id: " + id)
        );

        // Actualizar solo los campos que no son null
        if (songRequestDto.getTitle() != null) {
            song.setTittle(songRequestDto.getTitle());
        }
        if (songRequestDto.getDescription() != null) {
            song.setDescription(songRequestDto.getDescription());
        }
        if (songRequestDto.getVisibility() != null) {
            song.setVisibility(songRequestDto.getVisibility());
        }

        Song updatedSong = songRepository.save(song);
        return convertToResponseDto(updatedSong);
    }

    public void deleteSong(Long id) {
        Song song = songRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Canción no encontrada con id: " + id)
        );
        songRepository.delete(song);
    }

    public List<SongResponseDto> getSongsByUser(Long idUser) {
        return songRepository.findByIdUser(idUser).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<SongResponseDto> getPublicSongs() {
        return songRepository.findByVisibility("public").stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // Método auxiliar para convertir Song a SongResponseDto
    private SongResponseDto convertToResponseDto(Song song) {
        return new SongResponseDto(
            song.getIdSong(),
            song.getIdUser(),
            song.getTittle(),
            song.getDescription(),
            song.getCoverURL(),
            song.getFileURL(),
            song.getVisibility(),
            song.getUploadDate()
        );
    }
}
