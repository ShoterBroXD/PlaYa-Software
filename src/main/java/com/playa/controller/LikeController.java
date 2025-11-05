package com.playa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.playa.service.LikeService;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // POST /api/v1/songs/{id}/like - Dar like a canción
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeSong(
            @PathVariable("id") Long idSong,
            @RequestHeader("idUser") Long idUser){
        likeService.addLike(idSong, idUser);
        return new ResponseEntity<>("Like agregado",HttpStatus.CREATED);
    }

    // DELETE /api/v1/songs/{id}/like - Quitar like
    @DeleteMapping("/{id}/like")
    public ResponseEntity<String> unlikeSong(
            @PathVariable("id") Long idSong,
            @RequestHeader("idUser") Long idUser){
        likeService.removeLike(idSong, idUser);
        return ResponseEntity.ok("Like removido");
    }
}
