package com.tralaleros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tralaleros.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    // Métodos personalizados si los necesitas
}
