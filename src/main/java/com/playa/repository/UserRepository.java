package com.playa.repository;

import com.playa.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.playa.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Crear metodo Findbybyid()
    User findById(long id);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    /*@Query("SELECT u FROM User u " +
            "WHERE (:role IS NULL OR u.type = :role )" +
            "AND (:name IS NULL OR LOWER(CAST(u.name AS string)) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:idgenre IS NULL OR u.idgenre = :idgenre)")
    List<User> findArtistsByFilters(@Param("role") Rol role ,
                                    @Param("name") String name,
                                    @Param("idgenre") Long idgenre);*/
}