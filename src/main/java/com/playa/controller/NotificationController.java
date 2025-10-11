package com.playa.controller;

import com.playa.dto.NotificationPreferenceRequestDto;
import com.playa.dto.NotificationRequestDto;
import com.playa.dto.NotificationResponseDto;
import com.playa.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // POST /notifications - Crear notificación
    @PostMapping
    public ResponseEntity<NotificationResponseDto> createNotification(@Valid @RequestBody NotificationRequestDto requestDto) {
        NotificationResponseDto responseDto = notificationService.createNotification(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // PUT /notifications/{id} - Marcar como leída
    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> markAsRead(@PathVariable Long id) {
        NotificationResponseDto responseDto = notificationService.markAsRead(id);
        return ResponseEntity.ok(responseDto);
    }

    // GET /notifications/{idUser} - Listar notificaciones de usuario
    @GetMapping("/{idUser}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUser(@PathVariable Long idUser) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByUser(idUser);
        return ResponseEntity.ok(notifications);
    }

    // GET /notifications/{idUser}/unread - Obtener notificaciones no leídas
    @GetMapping("/{idUser}/unread")
    public ResponseEntity<List<NotificationResponseDto>> getUnreadNotificationsByUser(@PathVariable Long idUser) {
        List<NotificationResponseDto> notifications = notificationService.getUnreadNotificationsByUser(idUser);
        return ResponseEntity.ok(notifications);
    }

    // GET /notifications/{idUser}/count - Obtener cantidad de notificaciones no leídas
    @GetMapping("/{idUser}/count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long idUser) {
        long count = notificationService.getUnreadCount(idUser);
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

    // DELETE /notifications/{id} - Eliminar notificación
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notificación eliminada exitosamente");
    }
}
