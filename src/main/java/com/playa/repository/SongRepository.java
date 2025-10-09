package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.Song;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // Métodos personalizados para encontrar canciones por usuario
    List<Song> findByIdUser(Long idUser);

    // Método para encontrar canciones por visibilidad
    List<Song> findByVisibility(String visibility);

    // Método para encontrar canciones públicas de un usuario
    List<Song> findByIdUserAndVisibility(Long idUser, String visibility);
}
