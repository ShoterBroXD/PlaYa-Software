package com.playa.controller;

import com.playa.dto.response.ArtistPopularityResponse;
import com.playa.dto.response.ArtistReportResponse;
import com.playa.service.AnalysisReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class AnalysisReportController {

    private final AnalysisReportService reportService;

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<ArtistReportResponse> getArtistReport(@PathVariable Long artistId) {
        return ResponseEntity.ok(reportService.getArtistReport(artistId));
    }

    @GetMapping("/global/top-songs")
    public ResponseEntity<List<Map<String, Object>>> getGlobalTopSongs() {
        return ResponseEntity.ok(reportService.getGlobalTopSongs());
    }

    @GetMapping("/artist-popularity")
    public ResponseEntity<List<ArtistPopularityResponse>> getArtistPopularity(
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return ResponseEntity.ok(reportService.getArtistPopularityReport(genreId, startDate, endDate));
    }
}

