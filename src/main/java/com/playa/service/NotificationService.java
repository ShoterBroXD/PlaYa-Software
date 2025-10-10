package com.playa.service;

import com.playa.model.User;
import com.playa.dto.NotificationPreferenceRequestDto;
import com.playa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playa.repository.NotificationRepository;
import com.playa.model.Notification;
import com.playa.dto.NotificationRequestDto;
import com.playa.dto.NotificationResponseDto;
import com.playa.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    // Crear nueva notificación
    public NotificationResponseDto createNotification(NotificationRequestDto notificationRequestDto) {
        Notification notification = new Notification();
        notification.setIdUser(notificationRequestDto.getIdUser());
        notification.setContent(notificationRequestDto.getContent());
        notification.setDate(notificationRequestDto.getDate() != null ?
                            notificationRequestDto.getDate() : LocalDateTime.now());
        notification.setRead(false);

        Notification savedNotification = notificationRepository.save(notification);
        return convertToResponseDto(savedNotification);
    }

    // Marcar notificación como leída
    public NotificationResponseDto markAsRead(Long idNotification) {
        Notification notification = notificationRepository.findById(idNotification).orElseThrow(
            () -> new ResourceNotFoundException("Notificación no encontrada con id: " + idNotification)
        );

        notification.setRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return convertToResponseDto(updatedNotification);
    }

    // Obtener todas las notificaciones de un usuario
    public List<NotificationResponseDto> getNotificationsByUser(Long idUser) {
        return notificationRepository.findByUser_IdUserOrderByDateDesc(idUser).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // Obtener notificaciones no leídas de un usuario
    public List<NotificationResponseDto> getUnreadNotificationsByUser(Long idUser) {
        return notificationRepository.findByUser_IdUserAndReadFalse(idUser).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // Contar notificaciones no leídas
    public long countUnreadNotifications(Long idUser) {
        return notificationRepository.countByUser_IdUserAndReadFalse(idUser);
    }

    // Obtener una notificación específica
    public Optional<NotificationResponseDto> getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .map(this::convertToResponseDto);
    }

    //Método auxiliar para convertir Notification a NotificationResponseDto
    private NotificationResponseDto convertToResponseDto(Notification notification) {
        return new NotificationResponseDto(
            notification.getIdNotification(),
            notification.getIdUser(),
            notification.getContent(),
            notification.getDate(),
            notification.getRead()
        );
    }

    @Transactional
    public void updatePreferences(Long userId, NotificationPreferenceRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        // Por ahora, solo validamos que el usuario exista
    }
}
