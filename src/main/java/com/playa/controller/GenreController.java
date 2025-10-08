package com.playa.controller;

import com.playa.model.Genre;
import com.playa.dto.GenreResponseDto;
import com.playa.dto.SongResponseDto;
import com.playa.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public Genre createGenre(@RequestBody Genre genre) {
        return genreService.createGenre(genre.getName());
    }

    @GetMapping
    public ResponseEntity<List<GenreResponseDto>> getAllGenres() {
        List<GenreResponseDto> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<SongResponseDto>> getSongsByGenre(@PathVariable Long id) {
        List<SongResponseDto> songs = genreService.getSongsByGenre(id);
        return ResponseEntity.ok(songs);
    }
}
