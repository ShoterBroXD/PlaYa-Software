package com.playa.repository;

import com.playa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByIdgenre(Long idgenre);
    @Query("SELECT u FROM User u WHERE u.type = 'ARTIST' AND u.registerDate >= :threshold")
    List<User> findNewArtists(@Param("threshold") LocalDateTime threshold);

}
