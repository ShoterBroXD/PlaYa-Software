package com.playa.controller;

import com.playa.dto.NotificationPreferenceRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.playa.service.NotificationService;
import com.playa.dto.NotificationRequestDto;
import com.playa.dto.NotificationResponseDto;
import com.playa.exception.ResourceNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // POST /api/v1/notifications - Crear notificación
    @PostMapping
    public ResponseEntity<NotificationResponseDto> createNotification(@RequestBody NotificationRequestDto notificationRequestDto) {
        try {
            NotificationResponseDto createdNotification = notificationService.createNotification(notificationRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /api/v1/notifications/{id} - Marcar como leída
    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> markAsRead(@PathVariable Long id) {
        try {
            NotificationResponseDto updatedNotification = notificationService.markAsRead(id);
            return ResponseEntity.ok(updatedNotification);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/v1/notifications/{idUser} - Listar notificaciones de usuario
    @GetMapping("/{idUser}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUser(@PathVariable Long idUser) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByUser(idUser);
        return ResponseEntity.ok(notifications);
    }

    // GET /api/v1/notifications/unread/{idUser} - Obtener notificaciones no leídas
    @GetMapping("/unread/{idUser}")
    public ResponseEntity<List<NotificationResponseDto>> getUnreadNotificationsByUser(@PathVariable Long idUser) {
        List<NotificationResponseDto> unreadNotifications = notificationService.getUnreadNotificationsByUser(idUser);
        return ResponseEntity.ok(unreadNotifications);
    }

    // GET /api/v1/notifications/count/{idUser} - Contar notificaciones no leídas
    @GetMapping("/count/{idUser}")
    public ResponseEntity<Long> countUnreadNotifications(@PathVariable Long idUser) {
        long count = notificationService.countUnreadNotifications(idUser);
        return ResponseEntity.ok(count);
    }

    // Configurar preferencias
    @PutMapping("/preferences")
    public ResponseEntity<Void> updatePreferences(
            @RequestHeader("userId") Long userId,
            @RequestBody NotificationPreferenceRequestDto request) {
        notificationService.updatePreferences(userId, request);
        return ResponseEntity.ok().build();
    }
}
