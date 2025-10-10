package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    // Métodos personalizados si los necesitas

}
