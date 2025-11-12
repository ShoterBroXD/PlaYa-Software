package com.playa.service;

import com.playa.dto.response.ArtistPopularityResponse;
import com.playa.dto.response.ArtistReportResponse;
import com.playa.dto.response.SongPlayReportResponse;
import com.playa.model.SongHistory;
import com.playa.model.User;
import com.playa.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisReportService {

    private final AnalysisReportRepository reportRepository;
    private final SongRepository songRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final SongHistoryRepository playHistoryRepository;

    private final EntityManager entityManager;

    @Transactional
    public ArtistReportResponse getArtistReport(Long artistId) {
        User artist = userRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artista no encontrado"));

        List<SongPlayReportResponse> songStats = playHistoryRepository.findSongPlayStatsByArtist(artistId);

        if (songStats.isEmpty()) {
            return new ArtistReportResponse(artistId, artist.getName(), 0L, null, 0L, 0.0, List.of());
        }

        long total = songStats.stream().mapToLong(SongPlayReportResponse::getPlayCount).sum();
        SongPlayReportResponse topSong = songStats.get(0);
        double average = total / (double) songStats.size();

        return new ArtistReportResponse(
                artistId,
                artist.getName(),
                total,
                topSong.getTitle(),
                topSong.getPlayCount(),
                average,
                songStats
        );
    }

    @Transactional
    public List<Map<String, Object>> getGlobalTopSongs() {
        List<Object[]> results = reportRepository.getTopSongsGlobal();
        return results.stream().map(r -> Map.of(
                "title", r[0],
                "plays", r[1]
        )).toList();
    }

    @Transactional(readOnly = true)
    public List<ArtistPopularityResponse> getArtistPopularityReport(Long genreId, LocalDateTime startDate, LocalDateTime endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<SongHistory> root = cq.from(SongHistory.class);

        Join<Object, Object> songJoin = root.join("song");
        Join<Object, Object> artistJoin = songJoin.join("user");
        Join<Object, Object> genreJoin = songJoin.join("genre");

        List<Predicate> predicates = new ArrayList<>();

        if (genreId != null) {
            predicates.add(cb.equal(genreJoin.get("idgenre"), genreId));
        }

        if (startDate != null && endDate != null) {
            predicates.add(cb.between(root.get("playedAt"), startDate, endDate));
        }

        cq.multiselect(
                artistJoin.get("idUser"),
                artistJoin.get("name"),
                genreJoin.get("name"),
                cb.count(root.get("id"))
        );

        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(artistJoin.get("idUser"), artistJoin.get("name"), genreJoin.get("name"));
        cq.orderBy(cb.desc(cb.count(root.get("id"))));

        List<Object[]> results = entityManager.createQuery(cq).getResultList();

        return results.stream()
                .map(r -> new ArtistPopularityResponse(
                        (Long) r[0],
                        (String) r[1],
                        (String) r[2],
                        (Long) r[3]
                ))
                .collect(Collectors.toList());
    }
}

