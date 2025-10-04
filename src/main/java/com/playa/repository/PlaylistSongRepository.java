package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.playa.model.PlaylistSong;
import com.playa.model.PlaylistSongId;
import com.playa.model.Song;
import java.util.List;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId> {
    // Encontrar todas las canciones de una playlist
    @Query("SELECT ps FROM PlaylistSong ps WHERE ps.id.idPlaylist = :playlistId")
    List<PlaylistSong> findByIdPlaylist(@Param("playlistId") Long idPlaylist);

    // Verificar si una canción ya existe en una playlist
    @Query("SELECT CASE WHEN COUNT(ps) > 0 THEN true ELSE false END FROM PlaylistSong ps WHERE ps.id.idPlaylist = :playlistId AND ps.id.idSong = :songId")
    boolean existsByIdPlaylistAndIdSong(@Param("playlistId") Long idPlaylist, @Param("songId") Long idSong);

    // Eliminar una canción específica de una playlist
    @Query("DELETE FROM PlaylistSong ps WHERE ps.id.idPlaylist = :playlistId AND ps.id.idSong = :songId")
    void deleteByIdPlaylistAndIdSong(@Param("playlistId") Long idPlaylist, @Param("songId") Long idSong);

    // Obtener las canciones completas de una playlist usando JOIN
    @Query("SELECT s FROM Song s JOIN PlaylistSong ps ON s.idSong = ps.id.idSong WHERE ps.id.idPlaylist = :playlistId ORDER BY ps.date DESC")
    List<Song> findSongsByPlaylistId(@Param("playlistId") Long playlistId);
}
