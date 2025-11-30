package com.playa.controller;

import com.playa.dto.ArtistPopularityDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {

    @GetMapping("/artist-popularity")
    public ResponseEntity<List<ArtistPopularityDto>> getArtistPopularity(
            @RequestParam(required = false) Long idGenre,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        // Mock data: return empty list for now
        return ResponseEntity.ok(new ArrayList<>());
    }
}
