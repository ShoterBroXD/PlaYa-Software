package com.playa.service;

import com.playa.dto.NotificationPreferenceRequestDto;
import com.playa.dto.NotificationRequestDto;
import com.playa.dto.NotificationResponseDto;
import com.playa.exception.ResourceNotFoundException;
import com.playa.mapper.NotificationMapper;
import com.playa.model.Notification;
import com.playa.model.User;
import com.playa.repository.NotificationRepository;
import com.playa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public NotificationResponseDto createNotification(NotificationRequestDto requestDto) {
        if (!userRepository.existsById(requestDto.getIdUser())) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + requestDto.getIdUser());
        }

        Notification notification = Notification.builder()
                .idUser(requestDto.getIdUser())
                .content(requestDto.getContent())
                .date(requestDto.getDate() != null ? requestDto.getDate() : LocalDateTime.now())
                .read(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.convertToResponseDto(savedNotification);
    }

    @Transactional
    public NotificationResponseDto markAsRead(Long idNotification) {
        Notification notification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + idNotification));

        notification.setRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return notificationMapper.convertToResponseDto(updatedNotification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getNotificationsByUser(Long idUser) {
        // Validar que el usuario existe
        if (!userRepository.existsById(idUser)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + idUser);
        }

        List<Notification> notifications = notificationRepository.findByIdUserOrderByDateDesc(idUser);
        return notifications.stream()
                .map(notificationMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getUnreadNotificationsByUser(Long idUser) {
        List<Notification> notifications = notificationRepository.findUnreadByIdUserOrderByDateDesc(idUser);
        return notifications.stream()
                .map(notificationMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notificación no encontrada con ID: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long idUser) {
        return notificationRepository.countByIdUserAndRead(idUser, false);
    }

    @Transactional
    public void updatePreferences(Long userId, NotificationPreferenceRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        // Aquí podrías actualizar preferencias si tienes campos en User
    }
}
