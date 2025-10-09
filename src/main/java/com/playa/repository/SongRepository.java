package com.playa.repository;

import com.playa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.playa.model.Song;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Long countByUserAndVisibility(User user, String visibility);

    @Query("SELECT s FROM Song s JOIN s.genre g WHERE g.idGenre = :genreId AND  s.visibility = 'public'")
    List<Song> findByGenreId(Long genreId);

    // Métodos personalizados para encontrar canciones por usuario
    List<Song> findByIdUser(Long idUser);

    // Método para encontrar canciones por visibilidad
    List<Song> findByVisibility(String visibility);

    // Método para encontrar canciones públicas de un usuario
    List<Song> findByIdUserAndVisibility(Long idUser, String visibility);

    Long countByUserAndVisibilityNot(User user, String visibility);
}
