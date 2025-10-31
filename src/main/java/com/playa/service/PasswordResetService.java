package com.playa.service;

import com.playa.dto.PasswordResetRequestDto;
import com.playa.exception.ResourceNotFoundException;
import com.playa.model.PasswordResetToken;
import com.playa.model.User;
import com.playa.repository.PasswordResetTokenRepository;
import com.playa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    //private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public String generateResetToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        tokenRepository.deleteByUser(user);

        // Crear un nuevo token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expirationDate(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        tokenRepository.save(resetToken);
        return token;
    }

    @Transactional
    public void resetPassword(String token, PasswordResetRequestDto request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null) {
            throw new ResourceNotFoundException("Token inválido");
        }
        if (resetToken.isExpired()) {
            throw new RuntimeException("El token ha expirado");
        }
        if (resetToken.isUsed()) {
            throw new RuntimeException("El token ya fue utilizado");
        }

        User user = resetToken.getUser();
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("La contraseña de este campo debe de coincidir con la nueva contraseña");
        }

        //user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}
