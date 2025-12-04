package com.playa.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultsDto {
    private List<GenreSearchDto> genres;
    private List<ArtistSearchDto> artists;
    private List<SongSearchDto> songs;
    private List<PlaylistSearchDto> playlists;
    private List<CommunitySearchDto> communities;
}

