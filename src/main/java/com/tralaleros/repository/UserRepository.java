package com.tralaleros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tralaleros.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Métodos personalizados si los necesitas
}