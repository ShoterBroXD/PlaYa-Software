package com.playa.controller;

import com.playa.dto.*;
import com.playa.model.enums.Rol;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.playa.service.UserService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // GET /api/v1/users - Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // POST /api/v1/users - Registrar nuevo usuario
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto user) {
        UserResponseDto createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
    
    // GET /api/v1/users/{id} - Consultar perfil de usuario
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // PUT /api/v1/users/{id} - Actualizar perfil
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userDetails) {
        try {
            UserResponseDto updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE /api/v1/users/{id} - Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // PUT /api/v1/users/{id}/preferences - Actualizar preferencias musicales (funcionanalidad premium)
    @PutMapping("/{id}/preferences")
    @PreAuthorize("(hasRole('LISTENER') or hasRole('ARTIST')) and #id == authentication.principal.userId")
    public ResponseEntity<?> updatePreferences(@PathVariable Long id, @RequestBody UserPreferencesDto preferencesDto) {
        List<String> genres = preferencesDto.getFavoriteGenres();
        if (genres == null || genres.isEmpty()) {
            return ResponseEntity.badRequest().body("Debes seleccionar al menos una preferencia");
        }
        if (genres.size() > 5) {
            return ResponseEntity.badRequest().body("Solo puedes seleccionar hasta 5 géneros favoritos");
        }
        userService.updateUserPreferences(id, genres);
        return ResponseEntity.ok("Preferencias actualizadas correctamente");
    }

    // POST /api/v1/users/{id}/preferences/reset - Reiniciar preferencias musicales
    @PostMapping("/{id}/preferences/reset")
    @PreAuthorize("(hasRole('LISTENER') or hasRole('ARTIST')) and #id == authentication.principal.userId")
    public ResponseEntity<?> resetPreferences(@PathVariable Long id) {
        userService.resetUserPreferences(id);
        return ResponseEntity.ok("Preferencias reiniciadas. Recibirás recomendaciones desde cero");
    }

    @GetMapping("/nuevos")
    public ResponseEntity<List<UserResponseDto>> getNewArtists(Rol role) {
        List<UserResponseDto> users = userService.getNewArtists();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(users);
    }

    // NUEVO: PUT /api/v1/users/{id}/settings/language - Actualizar idioma de interfaz
    @PutMapping("/{id}/settings/language")
    public ResponseEntity<?> updateLanguage(@PathVariable Long id, @RequestBody UpdateLanguageRequest request) {
        userService.updateUserLanguage(id, request.getLanguage());
        return ResponseEntity.ok("Idioma actualizado correctamente");
    }

    // NUEVO: PUT /api/v1/users/{id}/settings/privacy - Actualizar privacidad del historial
    @PutMapping("/{id}/settings/privacy")
    public ResponseEntity<?> updatePrivacy(@PathVariable Long id, @RequestBody UpdatePrivacyRequest request) {
        userService.updateUserHistoryVisibility(id, request.getHistoryVisible());
        return ResponseEntity.ok("Configuración de privacidad actualizada correctamente");}
}