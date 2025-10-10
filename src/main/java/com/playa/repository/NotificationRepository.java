package com.playa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.Notification;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Métodos personalizados para encontrar notificaciones por usuario
    List<Notification> findByIdUser(Long idUser);

    // Método para encontrar notificaciones no leídas de un usuario
    List<Notification> findByIdUserAndReadFalse(Long idUser);

    // Método para contar notificaciones no leídas
    long countByIdUserAndReadFalse(Long idUser);

    // Método para obtener notificaciones de un usuario ordenadas por fecha
    List<Notification> findByIdUserOrderByDateDesc(Long idUser);
}
