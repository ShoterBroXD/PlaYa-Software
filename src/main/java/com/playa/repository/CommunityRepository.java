package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.Community;
import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    // Métodos personalizados para encontrar comunidades por nombre
    List<Community> findByNameContainingIgnoreCase(String name);

    // Método para encontrar comunidades ordenadas por fecha de creación
    List<Community> findAllByOrderByCreationDateDesc();

    // Método para verificar si una comunidad existe por nombre
    boolean existsByName(String name);
}
