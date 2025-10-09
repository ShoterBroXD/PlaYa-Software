package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Crear metodo Findbybyid()
    User findById(long id);
}