package com.tralaleros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tralaleros.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Métodos personalizados si los necesitas
}
