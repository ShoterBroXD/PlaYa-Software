package com.playa.service;

import com.playa.dto.SongUploadRequestDto;
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
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private static final Long MAX_FREE_SONGS = 10L;
    private static final Set<String> ALLOWED_FILE_FORMATS = Set.of("mp3", "wav", "flac");

    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;

    public List<SongResponseDto> getAllSongs() {
        return songRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private void validateFileFormat(String fileURL) {
        String fileExtension = fileURL.substring(fileURL.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_FILE_FORMATS.contains(fileExtension)) {
            throw new IllegalArgumentException("Formato de archivo no permitido: " + fileExtension);
        }
    }

    public SongResponseDto createSong(Long userId, SongUploadRequestDto songRequestDto) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("Usuario no encontrado."));

        if(!user.getPremium()){
            Long activeSongs=songRepository.countByUserAndVisibilityNot(user,"deleted");
            if(activeSongs>=MAX_FREE_SONGS){
                throw new IllegalStateException("Los usuarios gratuitos no pueden subir más de " + MAX_FREE_SONGS + " canciones. Actualiza a premium para subir más canciones.");
            }
        }

        validateFileFormat(songRequestDto.getFileURL());

        Song song=new Song();
        song.setUser(user);
        song.setTitle(songRequestDto.getTitle());
        song.setDescription(songRequestDto.getDescription());
        song.setCoverURL(songRequestDto.getCoverURL());
        song.setFileURL(songRequestDto.getFileURL());
        song.setVisibility(songRequestDto.getVisibility());
        song.setUploadDate(LocalDateTime.now());

        Genre genre=songRequestDto.getIdgenre()!=null?
                genreRepository.findById(songRequestDto.getIdgenre())
                        .orElseThrow(()->new ResourceNotFoundException("Género no encontrado.")):null;
        song.setGenre(genre);

        Song savedSong=songRepository.save(song);
        return convertToResponseDto(savedSong);

    }

    @Transactional(readOnly = true)
    public SongResponseDto getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con ID: " + id));
        return convertToResponseDto(song);
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

    public void deleteSong(Long id) {
        Song song = songRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Canción no encontrada con id: " + id)
        );
        songRepository.delete(song);
    }

    public List<SongResponseDto> getSongsByUser(Long idUser) {
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
        return SongResponseDto.builder()
                .idSong(song.getIdSong())
                .idUser(song.getUser().getIdUser())
                .title(song.getTitle())
                .description(song.getDescription())
                .coverURL(song.getCoverURL())
                .fileURL(song.getFileURL())
                .visibility(song.getVisibility())
                .uploadDate(song.getUploadDate())
                .build();
    }
}

