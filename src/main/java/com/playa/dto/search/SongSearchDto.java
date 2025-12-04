package com.playa.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongSearchDto {
    private Long id;
    private String title;
    private String artistName;
    private Float duration;
    private String coverURL;
}

