package com.playa.controller;

import com.playa.dto.AddSongToPlaylistDto;
import com.playa.dto.PlaylistRequestDto;
import com.playa.dto.PlaylistResponseDto;
import com.playa.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    // POST /api/v1/playlists - Crear playlist
    @PostMapping
    public ResponseEntity<PlaylistResponseDto> createPlaylist(@Valid @RequestBody PlaylistRequestDto requestDto) {
        PlaylistResponseDto responseDto = playlistService.createPlaylist(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // GET /api/v1/playlists/{id} - Obtener playlist
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponseDto> getPlaylistById(@PathVariable Long id) {
        PlaylistResponseDto responseDto = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(responseDto);
    }

    // POST /api/v1/playlists/{id}/songs - Agregar canción a playlist
    @PostMapping("/{id}/songs")
    public ResponseEntity<String> addSongToPlaylist(
            @PathVariable Long id,
            @Valid @RequestBody AddSongToPlaylistDto requestDto) {
        playlistService.addSongToPlaylist(id, requestDto);
        return ResponseEntity.ok("Canción agregada a la playlist exitosamente");
    }

    // DELETE /api/v1/playlists/{id}/songs/{songId} - Quitar canción de playlist
    @DeleteMapping("/{id}/songs/{songId}")
    public ResponseEntity<String> removeSongFromPlaylist(
            @PathVariable Long id,
            @PathVariable Long songId) {
        playlistService.removeSongFromPlaylist(id, songId);
        return ResponseEntity.ok("Canción removida de la playlist exitosamente");
    }

    // GET /api/v1/playlists - Obtener todas las playlists
    @GetMapping
    public ResponseEntity<List<PlaylistResponseDto>> getAllPlaylists() {
        List<PlaylistResponseDto> playlists = playlistService.getAllPlaylists();
        if (playlists.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(playlists); // 200
    }

    // GET /api/v1/playlists/user/{userId} - Obtener playlists de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistResponseDto>> getPlaylistsByUser(@PathVariable Long userId) {
        List<PlaylistResponseDto> playlists = playlistService.getPlaylistsByUser(userId);
        if (playlists.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(playlists); // 200
    }

    // POST /api/v1/playlists/{id}/report - Reportar/ocultar playlist
    @PostMapping("/{id}/report")
    public ResponseEntity<String> reportPlaylist(@PathVariable Long id) {
        playlistService.reportPlaylist(id);
        return ResponseEntity.ok("Playlist reportada y ocultada exitosamente");
    }

    // POST /api/v1/playlists/{id}/unreport - Mostrar playlist reportada
    @PostMapping("/{id}/unreport")
    public ResponseEntity<String> unreportPlaylist(@PathVariable Long id) {
        playlistService.unreportPlaylist(id);
        return ResponseEntity.ok("Playlist habilitada exitosamente");
    }

    // DELETE /api/v1/playlists/{id} - Eliminar playlist
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.ok("Playlist eliminada exitosamente");
    }
}
