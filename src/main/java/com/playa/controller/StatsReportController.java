package com.playa.controller;

import com.playa.dto.response.ArtistPopularityResponse;
import com.playa.dto.response.ArtistReportResponse;
import com.playa.service.StatsReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsReportController {

    private final StatsReportService reportService;

    @GetMapping("/artist/{artistId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ARTIST')")
    public ResponseEntity<ArtistReportResponse> getArtistReport(@PathVariable Long artistId) {
        return ResponseEntity.ok(reportService.getArtistReport(artistId));
    }

    @GetMapping("/global/top-songs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getGlobalTopSongs() {
        return ResponseEntity.ok(reportService.getGlobalTopSongs());
    }

    @GetMapping("/artist-popularity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ArtistPopularityResponse>> getArtistPopularity(
            @RequestParam(required = false) Long idGenre,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return ResponseEntity.ok(reportService.getArtistPopularityReport(idGenre, startDate, endDate));
    }
}

