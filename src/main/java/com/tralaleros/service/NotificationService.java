package com.tralaleros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tralaleros.repository.NotificationRepository;
import com.tralaleros.model.Notification;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Métodos de lógica de negocio para notificaciones
}
