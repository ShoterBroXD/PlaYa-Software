package com.playa.controller;

import com.playa.dto.search.SearchResultsDto;
import com.playa.service.CommunityService;
import com.playa.service.GenreService;
import com.playa.service.PlaylistService;
import com.playa.service.SongService;
import com.playa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final GenreService genreService;
    private final UserService userService;
    private final SongService songService;
    private final PlaylistService playlistService;
    private final CommunityService communityService;

    @GetMapping
    public ResponseEntity<SearchResultsDto> search(@RequestParam String query) {
        if (query == null || query.trim().length() < 2) {
            return ResponseEntity.badRequest().build();
        }

        String sanitizedQuery = query.trim();

        SearchResultsDto results = SearchResultsDto.builder()
                .genres(genreService.searchGenres(sanitizedQuery, 5))
                .artists(userService.searchArtists(sanitizedQuery, 5))
                .songs(songService.searchSongs(sanitizedQuery, 5))
                .playlists(playlistService.searchPlaylists(sanitizedQuery, 5))
                .communities(communityService.searchCommunities(sanitizedQuery, 5))
                .build();

        return ResponseEntity.ok(results);
    }
}

