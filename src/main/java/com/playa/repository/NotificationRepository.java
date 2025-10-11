package com.playa.repository;

import com.playa.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Métodos personalizados para encontrar notificaciones por usuario
    List<Notification> findByUser_IdUser(Long idUser);

    // Método para encontrar notificaciones no leídas de un usuario
    List<Notification> findByUser_IdUserAndReadFalse(Long idUser);

    // Método para contar notificaciones no leídas
    long countByUser_IdUserAndReadFalse(Long idUser);

    // Método para obtener notificaciones de un usuario ordenadas por fecha
    List<Notification> findByUser_IdUserOrderByDateDesc(Long idUser);

    List<Notification> findByIdUserAndRead(Long idUser, Boolean read);

    @Query("SELECT n FROM Notification n WHERE n.idUser = :idUser ORDER BY n.date DESC")
    List<Notification> findByIdUserOrderByDateDesc(@Param("idUser") Long idUser);

    @Query("SELECT n FROM Notification n WHERE n.idUser = :idUser AND n.read = false ORDER BY n.date DESC")
    List<Notification> findUnreadByIdUserOrderByDateDesc(@Param("idUser") Long idUser);

    // Método para contar notificaciones no leídas
    long countByIdUserAndRead(Long idUser, Boolean read);
}
