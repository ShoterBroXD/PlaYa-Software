package com.playa.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistSearchDto {
    private Long id;
    private String name;
    private String ownerName;
    private Integer trackCount;
}

