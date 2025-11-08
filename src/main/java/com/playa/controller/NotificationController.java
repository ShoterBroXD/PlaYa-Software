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

    // GET /notifications - Listar notificaciones de usuario
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUser(@RequestHeader Long idUser, @RequestParam(required = false, defaultValue = "false") Boolean unreadOnly) {
        List<NotificationResponseDto> notifications = notificationService.getUserNotifications(idUser, unreadOnly);
        return ResponseEntity.ok(notifications);
    }

    // PUT /notifications/{id} - Marcar como leída
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDto> markAsRead(@RequestHeader("idUser") Long iduser,@PathVariable Long id) {
        notificationService.markAsRead(iduser,id);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<Void> updatePreferences(@RequestHeader("idUser") Long idUser,@RequestBody NotificationPreferenceRequestDto request) {
        notificationService.updatePreferences(idUser, request);
        return ResponseEntity.ok().build();
    }

    // DELETE /notifications/{id} - Eliminar notificación
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notificación eliminada exitosamente");
    }
}
