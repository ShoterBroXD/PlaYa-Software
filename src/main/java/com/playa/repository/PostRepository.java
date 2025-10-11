package com.playa.repository;

import com.playa.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

interface PostRepository extends JpaRepository<Post, PK> {
}
