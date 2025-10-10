package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.Playlist;
import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    // Métodos personalizados para encontrar playlists por usuario
    List<Playlist> findByIdUser(Long idUser);

    // Método para encontrar playlists por nombre
    List<Playlist> findByNameContainingIgnoreCase(String name);

    // Método para obtener playlists de un usuario ordenadas por fecha de creación
    List<Playlist> findByIdUserOrderByCreationDateDesc(Long idUser);
}
