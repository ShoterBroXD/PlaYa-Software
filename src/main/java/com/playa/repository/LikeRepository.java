package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    // Métodos personalizados si los necesitas
}
