package com.playa.controller;

import com.playa.dto.HistoryRequestDto;
import com.playa.dto.HistoryResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.playa.service.HistoryService;

@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    // POST /api/v1/history - Registrar reproducción
    @PostMapping
    public ResponseEntity<HistoryResponseDto> createHistory(@RequestBody HistoryRequestDto historyRequestDto) {
        try {
            HistoryResponseDto History = historyService.createHistory(historyRequestDto);
            return ResponseEntity.status(201).body(History);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/v1/history/{idUser} - Historial de usuario
    @GetMapping("/{idUser}")
    public ResponseEntity<HistoryResponseDto> getHistoryByUserId(@PathVariable Long idUser) {
        try {
            HistoryResponseDto history = historyService.getHistoryByUserId(idUser);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
