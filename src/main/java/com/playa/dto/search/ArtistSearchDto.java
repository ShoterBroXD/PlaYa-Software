package com.playa.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistSearchDto {
    private Long id;
    private String name;
    private String genreName;
    private Long followerCount;
}

