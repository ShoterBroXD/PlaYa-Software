package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Métodos personalizados si los necesitas
}
