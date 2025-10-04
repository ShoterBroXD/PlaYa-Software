package com.playa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.playa.service.PlaylistService;
import com.playa.dto.PlaylistRequestDto;
import com.playa.dto.PlaylistResponseDto;
import com.playa.dto.AddSongToPlaylistDto;
import com.playa.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    // POST /api/v1/playlists - Crear playlist
    @PostMapping
    public ResponseEntity<PlaylistResponseDto> createPlaylist(@RequestBody PlaylistRequestDto playlistRequestDto) {
        try {
            PlaylistResponseDto createdPlaylist = playlistService.createPlaylist(playlistRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPlaylist);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/v1/playlists/{id} - Obtener playlist
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponseDto> getPlaylistById(@PathVariable Long id) {
        Optional<PlaylistResponseDto> playlist = playlistService.getPlaylistById(id);
        return playlist.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/v1/playlists/{id}/songs - Agregar canción a playlist
    @PostMapping("/{id}/songs")
    public ResponseEntity<String> addSongToPlaylist(@PathVariable Long id, @RequestBody AddSongToPlaylistDto addSongDto) {
        try {
            playlistService.addSongToPlaylist(id, addSongDto);
            return ResponseEntity.ok("Canción agregada exitosamente a la playlist");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE /api/v1/playlists/{id}/songs/{songId} - Quitar canción de playlist
    @DeleteMapping("/{id}/songs/{songId}")
    public ResponseEntity<String> removeSongFromPlaylist(@PathVariable Long id, @PathVariable Long songId) {
        try {
            playlistService.removeSongFromPlaylist(id, songId);
            return ResponseEntity.ok("Canción eliminada exitosamente de la playlist");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/v1/playlists/user/{userId} - Obtener playlists de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistResponseDto>> getPlaylistsByUser(@PathVariable Long userId) {
        List<PlaylistResponseDto> playlists = playlistService.getPlaylistsByUser(userId);
        return ResponseEntity.ok(playlists);
    }
}
