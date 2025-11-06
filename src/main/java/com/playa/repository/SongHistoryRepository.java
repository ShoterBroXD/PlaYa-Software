package com.playa.repository;

import com.playa.model.SongHistory;
import com.playa.model.SongHistoryId;
import com.playa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongHistoryRepository extends JpaRepository<SongHistory, SongHistoryId> {

    //metodo de consulta para obtener el historial de canciones de un usuario ordenado por fecha de reproduccion descendente
    List<SongHistory> findByUserOrderByDatePlayedDesc(User user);

}
