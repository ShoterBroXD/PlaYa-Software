package com.playa.controller;

import com.playa.dto.PasswordChangeRequestDto;
import com.playa.dto.PasswordResetRequestDto;
import com.playa.service.PasswordResetService;
import com.playa.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PasswordController {
    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @Valid @RequestBody PasswordChangeRequestDto request){
        userService.changePassword(id, request);
        return ResponseEntity.ok("Contraseña cambiada exitosamente");
    }

    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody PasswordResetRequestDto request) {
        passwordResetService.resetPassword(token, request);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    @PostMapping("/password/request")
    public ResponseEntity<String> requestReset(@RequestParam String email) {
        String token = passwordResetService.generateResetToken(email);
        return ResponseEntity.ok("Token generado: " + token);
    }

}
