package com.tralaleros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tralaleros.service.NotificationService;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // POST /api/v1/notifications - Crear notificación
    // PUT /api/v1/notifications/{id} - Marcar como leída
    // GET /api/v1/notifications/{idUser} - Listar notificaciones de usuario
}
