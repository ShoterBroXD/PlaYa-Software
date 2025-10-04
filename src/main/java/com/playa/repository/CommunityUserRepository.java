package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.playa.model.CommunityUser;
import com.playa.model.CommunityUserId;
import com.playa.model.User;
import java.util.List;

@Repository
public interface CommunityUserRepository extends JpaRepository<CommunityUser, CommunityUserId> {
    // Encontrar todos los miembros de una comunidad
    @Query("SELECT cu FROM CommunityUser cu WHERE cu.id.idCommunity = :communityId")
    List<CommunityUser> findByIdCommunity(@Param("communityId") Long idCommunity);

    // Verificar si un usuario ya es miembro de una comunidad
    @Query("SELECT CASE WHEN COUNT(cu) > 0 THEN true ELSE false END FROM CommunityUser cu WHERE cu.id.idCommunity = :communityId AND cu.id.idUser = :userId")
    boolean existsByIdCommunityAndIdUser(@Param("communityId") Long idCommunity, @Param("userId") Long idUser);

    // Obtener los usuarios completos de una comunidad usando JOIN
    @Query("SELECT u FROM User u JOIN CommunityUser cu ON u.idUser = cu.id.idUser WHERE cu.id.idCommunity = :communityId ORDER BY cu.joinDate DESC")
    List<User> findUsersByCommunityId(@Param("communityId") Long idCommunity);

    // Contar miembros de una comunidad
    @Query("SELECT COUNT(cu) FROM CommunityUser cu WHERE cu.id.idCommunity = :communityId")
    long countMembersByCommunityId(@Param("communityId") Long idCommunity);
}
