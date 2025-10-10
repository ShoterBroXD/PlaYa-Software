package com.playa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.playa.repository.PlaylistRepository;
import com.playa.repository.PlaylistSongRepository;
import com.playa.repository.SongRepository;
import com.playa.model.Playlist;
import com.playa.model.PlaylistSong;
import com.playa.model.Song;
import com.playa.dto.PlaylistRequestDto;
import com.playa.dto.PlaylistResponseDto;
import com.playa.dto.SongResponseDto;
import com.playa.dto.AddSongToPlaylistDto;
import com.playa.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private PlaylistRepository playlistRepository;
    private PlaylistSongRepository playlistSongRepository;
    private SongRepository songRepository;

    // Crear nueva playlist
    public PlaylistResponseDto createPlaylist(PlaylistRequestDto playlistRequestDto) {
        Playlist playlist = new Playlist();
        playlist.setIdUser(playlistRequestDto.getIdUser());
        playlist.setName(playlistRequestDto.getName());
        playlist.setDescription(playlistRequestDto.getDescription());
        playlist.setCreationDate(LocalDateTime.now());

        Playlist savedPlaylist = playlistRepository.save(playlist);
        return convertToResponseDto(savedPlaylist, null);
    }

    // Obtener playlist por ID incluyendo las canciones
    @Transactional(readOnly = true)
    public Optional<PlaylistResponseDto> getPlaylistById(Long id) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(id);
        if (playlistOpt.isEmpty()) {
            return Optional.empty();
        }
        List<Song> songs = playlistSongRepository.findSongsByPlaylistId(id);
        List<SongResponseDto> songDtos = songs.stream()
                .map(this::convertSongToResponseDto) // este método debe existir y convertir Song a SongResponseDto
                .toList();
        return Optional.of(convertToResponseDto(playlistOpt.get(), songDtos));
    }

    // Agregar canción a playlist
    @Transactional
    public void addSongToPlaylist(Long playlistId, AddSongToPlaylistDto addSongDto) {
        // Verificar que la playlist existe
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(
            () -> new ResourceNotFoundException("Playlist no encontrada con id: " + playlistId)
        );

        // Verificar que la canción existe
        Song song = songRepository.findById(addSongDto.getIdSong()).orElseThrow(
            () -> new ResourceNotFoundException("Canción no encontrada con id: " + addSongDto.getIdSong())
        );

        // Verificar si la canción ya está en la playlist
        if (playlistSongRepository.existsByIdPlaylistAndIdSong(playlistId, addSongDto.getIdSong())) {
            throw new RuntimeException("La canción ya existe en esta playlist");
        }

        // Agregar la canción a la playlist
        PlaylistSong playlistSong = new PlaylistSong(playlistId, addSongDto.getIdSong());
        playlistSongRepository.save(playlistSong);
    }

    // Quitar canción de playlist
    @Transactional
    public void removeSongFromPlaylist(Long playlistId, Long songId) {

        playlistRepository.findById(playlistId).orElseThrow(
            () -> new ResourceNotFoundException("Playlist no encontrada con id: " + playlistId)
        );


        if (!playlistSongRepository.existsByIdPlaylistAndIdSong(playlistId, songId)) {
            throw new ResourceNotFoundException("La canción no está en esta playlist");
        }

        playlistSongRepository.deleteByIdPlaylistAndIdSong(playlistId, songId);
    }

    // Obtener playlists de un usuario
    public List<PlaylistResponseDto> getPlaylistsByUser(Long idUser) {
        return playlistRepository.findByIdUserOrderByCreationDateDesc(idUser).stream()
                .map(playlist -> convertToResponseDto(playlist, null))
                .collect(Collectors.toList());
    }



    // Método auxiliar para convertir Song a SongResponseDto
    private PlaylistResponseDto convertToResponseDto(Playlist playlist, List<SongResponseDto> songs) {
        return new PlaylistResponseDto(
            playlist.getIdPlaylist(),
            playlist.getIdUser(),
            playlist.getName(),
            playlist.getDescription(),
            playlist.getCreationDate(),
            songs
        );
    }

    private SongResponseDto convertSongToResponseDto(Song song) {
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
