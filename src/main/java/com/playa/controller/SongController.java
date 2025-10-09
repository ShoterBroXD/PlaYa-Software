package com.playa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.playa.service.SongService;
import com.playa.dto.SongRequestDto;
import com.playa.dto.SongResponseDto;
import com.playa.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

    @Autowired
    private SongService songService;

    // POST /api/v1/songs - Subir canción
    @PostMapping
    public ResponseEntity<SongResponseDto> createSong(@RequestBody SongRequestDto songRequestDto) {
        try {
            SongResponseDto createdSong = songService.createSong(songRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSong);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/v1/songs/{id} - Consultar detalles canción
    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDto> getSongById(@PathVariable Long id) {
        Optional<SongResponseDto> song = songService.getSongById(id);
        return song.map(s -> ResponseEntity.ok(s))
                  .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/v1/songs/{id} - Obtener canción para reproducir
    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDto> getSong(@PathVariable Long id) {
        SongResponseDto response = songService.getSongById(id);
        return ResponseEntity.ok(response);
    }

    // PUT /api/v1/songs/{id} - Actualizar canción
    @PutMapping("/{id}")
    public ResponseEntity<SongResponseDto> updateSong(@PathVariable Long id, @RequestBody SongRequestDto songRequestDto) {
        try {
            SongResponseDto updatedSong = songService.updateSong(id, songRequestDto);
            return ResponseEntity.ok(updatedSong);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE /api/v1/songs/{id} - Eliminar canción
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        try {
            songService.deleteSong(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/v1/songs - Obtener todas las canciones públicas
    @GetMapping
    public ResponseEntity<List<SongResponseDto>> getPublicSongs() {
        List<SongResponseDto> songs = songService.getPublicSongs();
        return ResponseEntity.ok(songs);
    }

    // GET /api/v1/songs/user/{userId} - Obtener canciones de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SongResponseDto>> getSongsByUser(@PathVariable Long userId) {
        List<SongResponseDto> songs = songService.getSongsByUser(userId);
        return ResponseEntity.ok(songs);
    }
}
