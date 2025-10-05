package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.playa.model.History;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    // Métodos personalizados si los necesitas

    @Modifying
    @Transactional
    @Query(name = "INSERT INTO history (idUser, idSong, timestamp) VALUES (idUser, idSong, )")
    void registerHistory(Long idUser, Long idSong);

}
