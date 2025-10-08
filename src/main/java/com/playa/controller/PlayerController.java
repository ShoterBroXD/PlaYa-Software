package com.playa.controller;

import com.playa.dto.PlayHistoryRequestDto;
import com.playa.dto.SongResponseDto;
import com.playa.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    //Registrar reproducción
    @PostMapping("/play")
    public ResponseEntity<Void> registerPlay(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody PlayHistoryRequestDto request) {
        playerService.registerPlay(userId, request.getIdSong());
        return ResponseEntity.ok().build();
    }

    //Obtener historial
    @GetMapping("/history")
    public ResponseEntity<List<SongResponseDto>> getHistory(
            @RequestHeader("userId") Long userId) {
        List<SongResponseDto> history = playerService.getPlayHistory(userId);
        return ResponseEntity.ok(history);
    }
}