package com.tralaleros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tralaleros.model.Thread;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
    // Métodos personalizados si los necesitas
}
