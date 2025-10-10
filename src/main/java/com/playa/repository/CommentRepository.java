package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Métodos personalizados si los necesitas
}
