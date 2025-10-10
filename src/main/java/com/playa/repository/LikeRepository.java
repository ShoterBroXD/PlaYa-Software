package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.playa.model.Like;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    // Métodos personalizados si los necesitas

    //Encontrar todas las canciones que le gustan a un usuario
    @Query("SELECT l FROM Like l WHERE l.id.idUser = :userId")
    List<Like> findLikedSongsByUserId(@Param("userId") Long userId);

    boolean existsById_IdUserAndId_IdSong(Long idUser, Long idSong);

    Optional<Like> findById_IdUserAndId_IdSong(Long idUser, Long idSong);
}
