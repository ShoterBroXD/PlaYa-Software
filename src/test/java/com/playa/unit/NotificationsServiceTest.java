package com.playa.unit;

import com.playa.dto.NotificationPreferenceRequestDto;
import com.playa.dto.NotificationRequestDto;
import com.playa.dto.NotificationResponseDto;
import com.playa.exception.ResourceNotFoundException;
import com.playa.mapper.NotificationMapper;
import com.playa.model.Notification;
import com.playa.model.NotificationPreference;
import com.playa.model.User;
import com.playa.repository.NotificationPreferenceRepository;
import com.playa.repository.NotificationRepository;
import com.playa.repository.UserRepository;
import com.playa.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationsService - Pruebas Unitarias US-007")
class NotificationsServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationPreferenceRepository notificationPreferenceRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("CP-023 crear una notificación correctamente cuando el usuario existe")
    void createNotification_validUser_notificationCreated() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        NotificationRequestDto request = new NotificationRequestDto();
        request.setIdUser(1L);
        request.setContent("Nueva canción publicada");
        request.setType("COMMENT");

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent("Nueva canción publicada");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationMapper.convertToEntity(request)).thenReturn(notification);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(notificationMapper.convertToResponseDto(notification))
                .thenReturn(new NotificationResponseDto());

        // Act
        NotificationResponseDto response = notificationService.createNotification(request);

        // Assert
        assertNotNull(response);
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("usuario no existe al crear notificación")
    void createNotification_userNotFound_throwsException() {
        NotificationRequestDto request = new NotificationRequestDto();
        request.setIdUser(99L);
        request.setContent("Mensaje de prueba");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> notificationService.createNotification(request));

        verify(notificationRepository, never()).save(any());
    }

    @Test
    @DisplayName("usuario no tiene notificaciones")
    void getUserNotifications_noNotifications_returnsEmptyList() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.findByUserOrderByDateDesc(user.getIdUser())).thenReturn(List.of());

        // Act
        var result = notificationService.getUserNotifications(1L, false);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationRepository, times(1)).findByUserOrderByDateDesc(user.getIdUser());
    }

    @Test
    @DisplayName("marcar como leída ")
    void markAsRead_validNotification_updatesReadStatus() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        Notification notification = new Notification();
        notification.setIdNotification(10L);
        notification.setUser(user);
        notification.setRead(false);

        when(notificationRepository.findById(10L)).thenReturn(Optional.of(notification));

        // Act
        notificationService.markAsRead(10L, 1L);

        // Assert
        assertTrue(notification.getRead());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    @DisplayName("notificación no pertenece al usuario")
    void markAsRead_invalidUser_throwsException() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        Notification notification = new Notification();
        notification.setIdNotification(10L);
        notification.setUser(user);

        when(notificationRepository.findById(10L)).thenReturn(Optional.of(notification));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> notificationService.markAsRead(10L, 2L));

        verify(notificationRepository, never()).save(any());
    }

    @Test
    @DisplayName("CP-024 Configurar las preferencias de notificación correctamente")
    void setPreferences_validUser_updatesPreferences() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        NotificationPreference existingPreference = new NotificationPreference();
        existingPreference.setIdpreference(1L);
        existingPreference.setUser(user);
        existingPreference.setEnableComments(true);
        existingPreference.setEnableSystems(true);
        existingPreference.setEnableNewReleases(true);
        existingPreference.setEnableFollowers(true);

        NotificationPreferenceRequestDto request = new NotificationPreferenceRequestDto();
        request.setEnableComments(false);
        request.setEnableSystems(true);
        request.setEnableNewReleases(false);
        request.setEnableFollowers(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationPreferenceRepository.findByUser(user)).thenReturn(existingPreference);
        when(notificationPreferenceRepository.save(any(NotificationPreference.class)))
                .thenReturn(existingPreference);

        // Act
        notificationService.setpreferences(1L, request);

        // Assert
        assertFalse(existingPreference.getEnableComments());
        assertTrue(existingPreference.getEnableSystems());
        assertFalse(existingPreference.getEnableNewReleases());
        assertTrue(existingPreference.getEnableFollowers());
        verify(notificationPreferenceRepository, times(1)).save(existingPreference);
    }

    @Test
    @DisplayName("Debe crear nuevas preferencias si el usuario no las tiene")
    void setPreferences_noExistingPreferences_createsNew() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        NotificationPreferenceRequestDto request = new NotificationPreferenceRequestDto();
        request.setEnableComments(false);
        request.setEnableSystems(true);
        request.setEnableNewReleases(false);
        request.setEnableFollowers(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationPreferenceRepository.findByUser(user)).thenReturn(null);
        when(notificationPreferenceRepository.save(any(NotificationPreference.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        notificationService.setpreferences(1L, request);

        // Assert
        verify(notificationPreferenceRepository, times(1)).save(argThat(pref ->
                pref.getUser().equals(user) &&
                        !pref.getEnableComments() &&
                        pref.getEnableSystems() &&
                        !pref.getEnableNewReleases() &&
                        pref.getEnableFollowers()
        ));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el usuario no existe al actualizar preferencias")
    void setPreferences_userNotFound_throwsException() {
        // Arrange
        NotificationPreferenceRequestDto request = new NotificationPreferenceRequestDto();
        request.setEnableComments(false);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> notificationService.setpreferences(99L, request));

        verify(notificationPreferenceRepository, never()).save(any());
    }

    @Test
    @DisplayName("CP-025 Error al desactivar")
    void togglePreferences_existingPreferences_togglesAll() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        NotificationPreference preference = new NotificationPreference();
        preference.setIdpreference(1L);
        preference.setUser(user);
        preference.setEnableComments(true);
        preference.setEnableSystems(true);
        preference.setEnableNewReleases(false);
        preference.setEnableFollowers(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationPreferenceRepository.findByUser(user)).thenReturn(preference);
        when(notificationPreferenceRepository.save(any(NotificationPreference.class)))
                .thenReturn(preference);

        // Act
        notificationService.togglePreferences(1L);

        // Assert
        assertFalse(preference.getEnableComments());
        assertFalse(preference.getEnableSystems());
        assertTrue(preference.getEnableNewReleases());
        assertFalse(preference.getEnableFollowers());
        verify(notificationPreferenceRepository, times(1)).save(preference);
    }

    @Test
    @DisplayName("valores por defecto si no existen")
    void togglePreferences_noExistingPreferences_createsWithDefaults() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationPreferenceRepository.findByUser(user)).thenReturn(null);
        when(notificationPreferenceRepository.save(any(NotificationPreference.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        notificationService.togglePreferences(1L);

        // Assert
        verify(notificationPreferenceRepository, times(1)).save(argThat(pref ->
                pref.getUser().equals(user) &&
                        !pref.getEnableComments() &&
                        !pref.getEnableSystems() &&
                        !pref.getEnableNewReleases() &&
                        !pref.getEnableFollowers()
        ));
    }

    @Test
    @DisplayName("excepción si el usuario no existe al hacer toggle")
    void togglePreferences_userNotFound_throwsException() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> notificationService.togglePreferences(99L));

        verify(notificationPreferenceRepository, never()).save(any());
    }


}
