package com.playa.service;

import com.playa.dto.NotificationPreferenceRequestDto;
import com.playa.dto.NotificationRequestDto;
import com.playa.dto.NotificationResponseDto;
import com.playa.exception.ResourceNotFoundException;
import com.playa.mapper.NotificationMapper;
import com.playa.model.Notification;
import com.playa.model.NotificationPreference;
import com.playa.model.User;
import com.playa.model.enums.Category;
import com.playa.repository.NotificationPreferenceRepository;
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
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public NotificationResponseDto createNotification(NotificationRequestDto notificationRequestDto) {
        User user= userRepository.findById(notificationRequestDto.getIdUser())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Notification notification = notificationMapper.convertToEntity(notificationRequestDto);
        notification.setUser(user);
        notification.setContent(notificationRequestDto.getContent());
        notification.setRead(notificationRequestDto.getRead());
        notification.setDate(LocalDateTime.now());

        notificationRepository.save(notification);
        return notificationMapper.convertToResponseDto(notification);
    }

    @Transactional
    public void markAsRead(Long idUser,Long idNotification) {
        Notification notification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada "));

        if(!notification.getUser().getIdUser().equals(idUser)){
            throw new IllegalArgumentException("Notificación no pertenece al usuario especificado");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getUserNotifications(Long userId, Boolean unreadOnly) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Notification> notifications = unreadOnly
                ? notificationRepository.findByUserAndReadFalseOrderByDateDesc(user)
                : notificationRepository.findByUserOrderByDateDesc(user.getIdUser());

        return notifications.stream()
                .map(notificationMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getUnreadNotificationsByUser(Long idUser) {
        List<Notification> notifications = notificationRepository.findByUserIdUserAndReadFalseOrderByDateDesc(idUser);
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
        return notificationRepository.countByUserIdUserAndRead(idUser, false);
    }

    @Transactional
    public void updatePreferences(Long userId, NotificationPreferenceRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        NotificationPreference preference = notificationPreferenceRepository.findByUser(user);

        if(preference == null){
            preference = new NotificationPreference();
            preference.setUser(user);
        }

        preference.setEnableComments(request.getEnabledComments());
        preference.setEnableFollowers(request.getEnableFollowers());
        preference.setEnableSystems(request.getEnableSystems());
        preference.setEnableNewReleases(request.getEnableNewReleases());

        notificationPreferenceRepository.save(preference);
    }
}
