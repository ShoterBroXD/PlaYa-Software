package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Métodos personalizados si los necesitas
    List<User> findAllByType(String type);
}