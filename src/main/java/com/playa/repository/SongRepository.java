package com.playa.repository;

import com.playa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.Song;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Long countByUserAndVisibility(User user, String visibility);

    List<Song> findByGenres_IdGenreAndVisibility(Long genreId, String visibility);

    // Métodos personalizados para encontrar canciones por usuario
    List<Song> findByUser_IdUser(Long idUser);

    // Método para encontrar canciones por visibilidad
    List<Song> findByVisibility(String visibility);


    Long countByUserAndVisibilityNot(User user, String visibility);

    List<Song> findByUser(User user);
}
