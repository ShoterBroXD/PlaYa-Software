package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.Community;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    // Métodos personalizados si los necesitas
}
