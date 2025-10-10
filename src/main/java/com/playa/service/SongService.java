package com.playa.service;

import com.playa.dto.ArtistResponseDto;
import com.playa.dto.GenreResponseDto;
import com.playa.exception.SongLimitExceededException;
import com.playa.model.Genre;
import com.playa.model.User;
import com.playa.repository.GenreRepository;
import com.playa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playa.repository.SongRepository;
import com.playa.model.Song;
import com.playa.dto.SongRequestDto;
import com.playa.dto.SongResponseDto;
import com.playa.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenreRepository genreRepository;

    private static final Long MAX_FREE_SONGS = 10L;
    private static final Set<String> ALLOWED_FILE_FORMATS = Set.of("mp3", "wav", "flac");

    private void validateFileFormat(String fileURL) {
        int lastDot = fileURL.lastIndexOf('.');
        if (lastDot == -1 || lastDot == fileURL.length() - 1) {
            throw new IllegalArgumentException("El archivo no tiene extensión válida.");
        }
        String fileExtension = fileURL.substring(lastDot + 1).toLowerCase();
        if (!ALLOWED_FILE_FORMATS.contains(fileExtension)) {
            throw new IllegalArgumentException("Formato de archivo no permitido: " + fileExtension);
        }
    }

    public List<SongResponseDto> getAllSongs() {
        return songRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SongResponseDto createSong(Long userId, SongRequestDto songRequestDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!user.getPremium()) {
            Long activeSongs = songRepository.countByUserAndVisibilityNot(user, "deleted");
            if (activeSongs >= MAX_FREE_SONGS) {
                throw new SongLimitExceededException("Has alcanzado el límite de " + MAX_FREE_SONGS + " canciones. Actualiza a premium.");
            }
        }

        // Validar formato de archivo
        validateFileFormat(songRequestDto.getFileURL());

        Song song = new Song();
        song.setUser(user);
        song.setTitle(songRequestDto.getTitle());
        song.setDescription(songRequestDto.getDescription());
        song.setCoverURL(songRequestDto.getCoverURL());
        song.setFileURL(songRequestDto.getFileURL());
        song.setVisibility(songRequestDto.getVisibility());
        song.setUploadDate(LocalDateTime.now());

        if (songRequestDto.getGenres() != null) {
            Set<Genre> genres = songRequestDto.getGenres().stream()
                    .map(id -> genreRepository.findById(id.getIdGenre())
                            .orElseThrow(() -> new ResourceNotFoundException("Género no encontrado: " + id)))
                    .collect(Collectors.toSet());
            song.setGenres(genres);
        }

        Song savedSong = songRepository.save(song);
        return convertToResponseDto(savedSong);
    }

    public Optional<SongResponseDto> getSongById(Long id) {
        return songRepository.findById(id)
                .map(this::convertToResponseDto);
    }

    @Transactional
    public SongResponseDto updateSong(Long id, SongRequestDto songRequestDto) {
        Song song = songRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Canción no encontrada con id: " + id)
        );

        // Actualizar solo los campos que no son null
        if (songRequestDto.getTitle() != null) {
            song.setTitle(songRequestDto.getTitle());
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

    @Transactional
    public void deleteSong(Long id, Long idUser) {
        Song song = songRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Canción no encontrada con id: " + id)
        );
        if (!song.getIdUser().equals(idUser)) {
            throw new IllegalArgumentException("No tienes permiso para eliminar esta canción");
        }
        songRepository.delete(song);
    }

    public List<SongResponseDto> getSongsByUser(Long idUser) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return songRepository.findByUser_IdUser(idUser).stream()
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
            song.getTitle(),
            song.getDescription(),
            song.getCoverURL(),
            song.getFileURL(),
            song.getVisibility(),
            song.getUploadDate()
        );
    }

}
