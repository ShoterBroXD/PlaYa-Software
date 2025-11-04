package com.playa.controller;

import com.playa.dto.CommentResponseDto;
import com.playa.dto.SongRequestDto;
import com.playa.dto.SongResponseDto;
import com.playa.service.CommentService;
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
    private final CommentService commentService;

    // POST /api/v1/songs - Subir canción
    @PostMapping
    public ResponseEntity<SongResponseDto> createSong(@RequestHeader("iduser") Long idUser,@Valid @RequestBody SongRequestDto requestDto) {
        SongResponseDto responseDto = songService.createSong(idUser,requestDto);
        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);

    }

    // GET /api/v1/songs/{id} - Consultar detalles canción
    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDto> getSongById(@PathVariable Long id) {
        SongResponseDto responseDto = songService.getSongById(id);
        return ResponseEntity.ok(responseDto);
    }

    // PUT /api/v1/songs/{id} - Actualizar canción
    @PutMapping("/{id}")
    public ResponseEntity<SongResponseDto> updateSong(
            @PathVariable Long id,
            @Valid @RequestBody SongRequestDto requestDto) {
            SongResponseDto responseDto = songService.updateSong(id, requestDto);
            return ResponseEntity.ok(responseDto);
    }

    // DELETE /api/v1/songs/{id} - Eliminar canción
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable Long id) {
            songService.deleteSong(id);
            return ResponseEntity.ok("Canción eliminada exitosamente");
    }

    @GetMapping("/{idsong}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @PathVariable Long idsong) {
        List<CommentResponseDto> comments = songService.getAllComments(idsong);
        return ResponseEntity.ok(comments);
    }

    // GET /api/v1/songs/user/{userId} - Obtener canciones de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SongResponseDto>> getSongsByUser(@PathVariable Long userId) {
        List<SongResponseDto> songs = songService.getSongsByUser(userId);
        return ResponseEntity.ok(songs);
    }

    // GET /api/v1/songs - Obtener todas las canciones públicas
    @GetMapping("/public")
    public ResponseEntity<List<SongResponseDto>> getPublicSongs() {
        List<SongResponseDto> songs = songService.getPublicSongs();
        return ResponseEntity.ok(songs);
    }
}
