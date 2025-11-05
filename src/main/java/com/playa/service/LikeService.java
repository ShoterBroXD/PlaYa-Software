package com.playa.service;

import com.playa.model.Like;
import com.playa.model.LikeId;
import com.playa.model.Song;
import com.playa.model.User;
import com.playa.repository.SongRepository;
import com.playa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playa.repository.LikeRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    @Transactional
    public void addLike(Long idSong, Long idUser) {
        Song song = songRepository.findById(idSong)
                .orElseThrow(()->new RuntimeException("Canción no encontrada"));
        User user = userRepository.findById(idUser)
                .orElseThrow(()->new RuntimeException("Usuario no encontrado"));

        LikeId likeId = new LikeId(idUser, idSong);
        if (likeRepository.existsById(likeId)) {
            return;
        }

        Like like = new Like();
        like.setId(likeId);
        like.setDate(LocalDateTime.now());
        likeRepository.save(like);
    }

    @Transactional
    public void removeLike(Long idSong, Long idUser) {
        LikeId likeId = new LikeId(idUser, idSong);
        if (!likeRepository.existsById(likeId)) {
            throw new RuntimeException("Like no encontrado");
        }
        likeRepository.deleteById(likeId);
    }
}
