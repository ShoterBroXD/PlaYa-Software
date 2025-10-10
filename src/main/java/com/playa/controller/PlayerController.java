package com.playa.controller;

import com.playa.dto.*;
import com.playa.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/play")
    public ResponseEntity<Void> registerPlay(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody PlayHistoryRequestDto request) {
        playerService.registerPlay(userId, request.getIdSong());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<SongResponseDto>> getHistory(
            @RequestHeader("userId") Long userId) {
        List<SongResponseDto> history = playerService.getPlayHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/history/extended")
    public ResponseEntity<List<HistoryResponseDto>> getExtendedHistory(
            @RequestHeader("userId") Long userId,
            @RequestParam(required = false, defaultValue = "50") Integer limit) {
        List<HistoryResponseDto> history = playerService.getHistory(userId, limit);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/current")
    public ResponseEntity<CurrentPlaybackResponseDto> getCurrentPlayback(
            @RequestHeader("userId") Long userId) {
        CurrentPlaybackResponseDto response = playerService.getCurrentPlayback(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/play/song")
    public ResponseEntity<PlaybackControlResponseDto> playSong(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody PlaySongRequestDto request) {
        PlaybackControlResponseDto response = playerService.playSong(userId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/pause")
    public ResponseEntity<PlaybackControlResponseDto> pause(
            @RequestHeader("userId") Long userId) {
        PlaybackControlResponseDto response = playerService.pause(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/resume")
    public ResponseEntity<PlaybackControlResponseDto> resume(
            @RequestHeader("userId") Long userId) {
        PlaybackControlResponseDto response = playerService.resume(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/stop")
    public ResponseEntity<PlaybackControlResponseDto> stop(
            @RequestHeader("userId") Long userId) {
        PlaybackControlResponseDto response = playerService.stop(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/next")
    public ResponseEntity<PlaybackControlResponseDto> next(
            @RequestHeader("userId") Long userId) {
        PlaybackControlResponseDto response = playerService.next(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/previous")
    public ResponseEntity<PlaybackControlResponseDto> previous(
            @RequestHeader("userId") Long userId) {
        PlaybackControlResponseDto response = playerService.previous(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/shuffle")
    public ResponseEntity<Map<String, Object>> setShuffle(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody ShuffleRequestDto request) {
        Map<String, Object> response = playerService.setShuffle(userId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/repeat")
    public ResponseEntity<Map<String, Object>> setRepeatMode(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody RepeatModeRequestDto request) {
        Map<String, Object> response = playerService.setRepeatMode(userId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/volume")
    public ResponseEntity<Map<String, Object>> setVolume(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody VolumeRequestDto request) {
        Map<String, Object> response = playerService.setVolume(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/queue")
    public ResponseEntity<QueueResponseDto> getQueue(
            @RequestHeader("userId") Long userId) {
        QueueResponseDto response = playerService.getQueue(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/queue")
    public ResponseEntity<Map<String, Object>> addToQueue(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody AddToQueueRequestDto request) {
        Map<String, Object> response = playerService.addToQueue(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/queue/{position}")
    public ResponseEntity<Void> removeFromQueue(
            @RequestHeader("userId") Long userId,
            @PathVariable Integer position) {
        playerService.removeFromQueue(userId, position);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/recommendations")
    public ResponseEntity<List<RecommendationResponseDto>> getRecommendations(
            @RequestHeader("userId") Long userId) {
        List<RecommendationResponseDto> recommendations = playerService.getRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

}