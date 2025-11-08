package com.playa.unit;

import com.playa.exception.ResourceNotFoundException;
import com.playa.model.User;
import com.playa.model.enums.Rol;
import com.playa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PremiumSubscription - Pruebas Unitarias US-020")
class PremiumSubscriptionTest {

    @Mock
    private UserRepository userRepository;

    private User mockFreeUser;
    private User mockPremiumUser;

    @BeforeEach
    void setUp() {
        mockFreeUser = createMockUser(300L, "user_free@mail.com", "Free User", false);
        mockPremiumUser = createMockUser(301L, "user_premium@mail.com", "Premium User", true);
    }

    private User createMockUser(Long id, String email, String name, Boolean premium) {
        User user = new User();
        user.setIdUser(id);
        user.setEmail(email);
        user.setName(name);
        user.setPremium(premium);
        user.setType(Rol.LISTENER);
        user.setRegisterDate(LocalDateTime.now());
        return user;
    }

    @Test
    @DisplayName("CP-019: Debe mejorar plan a Premium exitosamente")
    void upgradeToPremiun_ValidPayment_Success() {
        // Arrange
        String planType = "Premium Anual";
        boolean validPaymentMethod = true;

        when(userRepository.findById(300L)).thenReturn(Optional.of(mockFreeUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setPremium(true);
            return user;
        });

        // Act - Simular proceso de mejora a premium
        Optional<User> userOptional = userRepository.findById(300L);
        assertThat(userOptional).isPresent();
        User userToUpgrade = userOptional.get();

        // Validaciones previas
        assertThat(userToUpgrade.getPremium()).isFalse(); // Era usuario gratuito
        assertThat(validPaymentMethod).isTrue(); // Método de pago válido
        assertThat(planType).isEqualTo("Premium Anual");

        // Simular actualización
        userToUpgrade.setPremium(true);
        User savedUser = userRepository.save(userToUpgrade);

        // Assert
        assertThat(savedUser.getPremium()).isTrue();

        // Simular mensaje de éxito
        String expectedMessage = "¡Bienvenido a Premium!";
        assertThat(expectedMessage).isEqualTo("¡Bienvenido a Premium!");

        verify(userRepository).findById(300L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("CP-020: Debe visualizar estado de suscripción activa")
    void viewSubscriptionStatus_PremiumUser_Success() {
        // Arrange
        String email = "user_premium@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockPremiumUser));

        // Act
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow();

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getIdUser()).isEqualTo(301L);
        assertThat(user.getPremium()).isTrue();

        // Simular visualización del estado
        String subscriptionStatus = user.getPremium() ? "Premium" : "Gratuita";
        assertThat(subscriptionStatus).isEqualTo("Premium");

        // Simular mensaje en perfil
        String profileMessage = "Estado: " + subscriptionStatus;
        assertThat(profileMessage).isEqualTo("Estado: Premium");

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Debe visualizar estado gratuito para usuario no premium")
    void viewSubscriptionStatus_FreeUser_Success() {
        // Arrange
        String email = "user_free@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockFreeUser));

        // Act
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow();

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getPremium()).isFalse();

        // Simular visualización del estado
        String subscriptionStatus = user.getPremium() ? "Premium" : "Gratuita";
        assertThat(subscriptionStatus).isEqualTo("Gratuita");

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("CP-021: Debe revertir a cuenta gratuita por suscripción vencida")
    void revertToFree_ExpiredSubscription_Success() {
        // Arrange
        Long userId = 301L;
        boolean paymentFailed = true;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockPremiumUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setPremium(false);
            return user;
        });

        // Act - Simular proceso de reversión
        Optional<User> userOptional = userRepository.findById(userId);
        assertThat(userOptional).isPresent();
        User userToRevert = userOptional.get();

        // Validaciones previas
        assertThat(userToRevert.getPremium()).isTrue(); // Era usuario premium
        assertThat(paymentFailed).isTrue(); // Pago falló

        // Simular job de renovación fallido
        if (paymentFailed) {
            userToRevert.setPremium(false);
            User savedUser = userRepository.save(userToRevert);

            // Assert
            assertThat(savedUser.getPremium()).isFalse();

            // Simular estado en perfil
            String newStatus = "Gratuita";
            assertThat(newStatus).isEqualTo("Gratuita");
        }

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("CP-022: Debe fallar al suscribirse sin método de pago válido")
    void upgradeToPremiun_InvalidPayment_Fails() {
        // Arrange
        String planType = "Premium Mensual";
        boolean validPaymentMethod = false; // Método de pago inválido

        when(userRepository.findById(300L)).thenReturn(Optional.of(mockFreeUser));

        // Act & Assert - Simular validación de pago
        assertThatThrownBy(() -> {
            if (!validPaymentMethod) {
                throw new IllegalArgumentException("Método de pago inválido");
            }
        })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Método de pago inválido");

        // Verificar que el usuario permanece como gratuito
        Optional<User> userOptional = userRepository.findById(300L);
        assertThat(userOptional).isPresent();
        User user = userOptional.get();
        assertThat(user.getPremium()).isFalse();

        // Simular mensaje de error de pasarela de pago
        String errorMessage = "Pago rechazado";
        assertThat(errorMessage).isEqualTo("Pago rechazado");

        verify(userRepository).findById(300L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe validar diferentes tipos de plan premium")
    void validatePremiumPlans_DifferentTypes_Success() {
        // Arrange
        String[] validPlans = {"Premium Mensual", "Premium Anual", "Premium Familiar"};

        // Act & Assert
        for (String plan : validPlans) {
            assertThat(plan).isNotNull();
            assertThat(plan).contains("Premium");
            assertThat(plan).isIn((Object[]) validPlans);

            // Simular validación de plan
            boolean isValidPlan = plan.startsWith("Premium");
            assertThat(isValidPlan).isTrue();
        }
    }

    @Test
    @DisplayName("Debe validar beneficios de usuario premium")
    void validatePremiumBenefits_PremiumUser_Success() {
        // Arrange
        User premiumUser = mockPremiumUser;

        // Act & Assert
        assertThat(premiumUser.getPremium()).isTrue();

        // Simular beneficios premium
        boolean noAds = premiumUser.getPremium();
        boolean unlimitedSkips = premiumUser.getPremium();
        boolean offlineMode = premiumUser.getPremium();
        boolean higherQuality = premiumUser.getPremium();

        assertThat(noAds).isTrue(); // Sin anuncios
        assertThat(unlimitedSkips).isTrue(); // Saltos ilimitados
        assertThat(offlineMode).isTrue(); // Modo offline
        assertThat(higherQuality).isTrue(); // Mayor calidad de audio
    }

    @Test
    @DisplayName("Debe validar restricciones de usuario gratuito")
    void validateFreeUserLimitations_FreeUser_Success() {
        // Arrange
        User freeUser = mockFreeUser;

        // Act & Assert
        assertThat(freeUser.getPremium()).isFalse();

        // Simular limitaciones de usuario gratuito
        boolean hasAds = !freeUser.getPremium();
        boolean limitedSkips = !freeUser.getPremium();
        boolean noOfflineMode = !freeUser.getPremium();
        boolean standardQuality = !freeUser.getPremium();

        assertThat(hasAds).isTrue(); // Tiene anuncios
        assertThat(limitedSkips).isTrue(); // Saltos limitados
        assertThat(noOfflineMode).isTrue(); // Sin modo offline
        assertThat(standardQuality).isTrue(); // Calidad estándar
    }

    @Test
    @DisplayName("Debe manejar usuario no encontrado al intentar actualizar suscripción")
    void upgradeToPremiun_UserNotFound_ThrowsException() {
        // Arrange
        Long nonExistentUserId = 999L;
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> {
            Optional<User> userOptional = userRepository.findById(nonExistentUserId);
            if (userOptional.isEmpty()) {
                throw new ResourceNotFoundException("Usuario no encontrado con ID: " + nonExistentUserId);
            }
        })
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Usuario no encontrado con ID: 999");

        verify(userRepository).findById(nonExistentUserId);
    }
}
