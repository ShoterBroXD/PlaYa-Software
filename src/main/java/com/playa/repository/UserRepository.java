package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.playa.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Métodos personalizados si los necesitas
    List<User> findAllByType(String type);

    @Query("SELECT u FROM User u WHERE u.email=: email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM users u WHERE u.type = 'ARTIST' AND u.registerdate >= (CURRENT_TIMESTAMP - INTERVAL '14 days')")
    List<User> findNewArtists();
}