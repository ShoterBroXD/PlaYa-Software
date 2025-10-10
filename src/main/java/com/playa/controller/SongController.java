package com.playa.controller;

import com.playa.dto.SongRequestDto;
import com.playa.dto.SongResponseDto;
import com.playa.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    // POST /songs - Subir canción
    @PostMapping
    public ResponseEntity<SongResponseDto> createSong(@Valid @RequestBody SongRequestDto requestDto) {
        SongResponseDto responseDto = songService.createSong(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // GET /songs/{id} - Consultar detalles canción
    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDto> getSongById(@PathVariable Long id) {
        SongResponseDto responseDto = songService.getSongById(id);
        return ResponseEntity.ok(responseDto);
    }

    // PUT /songs/{id} - Actualizar canción
    @PutMapping("/{id}")
    public ResponseEntity<SongResponseDto> updateSong(
            @PathVariable Long id,
            @Valid @RequestBody SongRequestDto requestDto) {
        SongResponseDto responseDto = songService.updateSong(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // DELETE /songs/{id} - Eliminar canción
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.ok("Canción eliminada exitosamente");
    }

    // GET /songs - Obtener todas las canciones
    @GetMapping
    public ResponseEntity<List<SongResponseDto>> getAllSongs() {
        List<SongResponseDto> songs = songService.getAllSongs();
        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(songs); // 200
    }

    // GET /songs/user/{userId} - Obtener canciones de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SongResponseDto>> getSongsByUser(@PathVariable Long userId) {
        List<SongResponseDto> songs = songService.getSongsByUser(userId);
        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(songs); // 200
    }

    // GET /songs/public - Obtener canciones públicas
    @GetMapping("/public")
    public ResponseEntity<List<SongResponseDto>> getPublicSongs() {
        List<SongResponseDto> songs = songService.getPublicSongs();
        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(songs); // 200
    }
}
