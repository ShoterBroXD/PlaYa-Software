package com.playa.service;

import com.playa.model.Like;
import com.playa.model.LikeId;
import org.springframework.stereotype.Service;
import com.playa.repository.LikeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }
    // Métodos de lógica de negocio para likes

    public void addLike(Long userId, Long songId) {
        if (userId == null || songId == null) throw new IllegalArgumentException("ids required");
        if (likeRepository.existsById_IdUserAndId_IdSong(userId, songId)) return;
        Like like = new Like(new LikeId(userId, songId), LocalDateTime.now());
        likeRepository.save(like);
    }

    public List<Like> getLikedSongIds(Long userId) {
        return likeRepository.findLikedSongsByUserId(userId);
    }

    public void removeLike(Long userId, Long songId) {
        likeRepository.findById_IdUserAndId_IdSong(userId, songId).ifPresent(likeRepository::delete);
    }

}
