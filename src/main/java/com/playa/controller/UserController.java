package com.playa.controller;

import com.playa.dto.SongResponseDto;
import com.playa.model.enums.Rol;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.playa.service.UserService;
import com.playa.dto.UserRequestDto;
import com.playa.dto.UserResponseDto;

import javax.management.relation.Role;
import com.playa.dto.UserPreferencesDto;
import java.util.List;

import com.playa.dto.UpdateLanguageRequest;
import com.playa.dto.UpdatePrivacyRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/v1/users - Obtener todos los usuarios (Solo ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(users); // 200
    }
    
    // POST /api/v1/users - Registrar nuevo usuario (Solo ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto user) {
        UserResponseDto createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
    
    // GET /api/v1/users/{id} - Consultar perfil de usuario (Usuario propietario o ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // PUT /api/v1/users/{id} - Actualizar perfil (Usuario propietario o ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userDetails) {
        try {
            UserResponseDto updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE /api/v1/users/{id} - Eliminar usuario (Usuario propietario o ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // GET /api/v1/users/artists/filter - Filtrar artistas (público)
    @GetMapping("/artists/filter")
    public ResponseEntity<List<UserResponseDto>> filterArtists(
            @RequestParam(required = false) Rol role,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long idgenre) {

        Rol rol = role != null ? role : Rol.ARTIST;
        List<UserResponseDto> result = userService.filterArtists(rol, name, idgenre);
        return ResponseEntity.ok(result);
    }

    // GET /api/v1/users/genre/{idgenre} - Obtener usuarios por género (público)
    @GetMapping("/genre/{idgenre}")
    public ResponseEntity<List<UserResponseDto>> getAllByIdGenre(@PathVariable Long idgenre) {
        List<UserResponseDto> users = userService.findAllByIdGenre(idgenre);
        return ResponseEntity.ok(users);
    }

    // PUT /api/v1/users/{id}/preferences - Actualizar preferencias musicales (Funcionalidad premium)
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

    // POST /api/v1/users/{id}/preferences/reset - Reiniciar preferencias musicales (Funcionalidad premium)
    @PostMapping("/{id}/preferences/reset")
    @PreAuthorize("(hasRole('LISTENER') or hasRole('ARTIST')) and #id == authentication.principal.userId")
    public ResponseEntity<?> resetPreferences(@PathVariable Long id) {
        userService.resetUserPreferences(id);
        return ResponseEntity.ok("Preferencias reiniciadas. Recibirás recomendaciones desde cero");
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
        return ResponseEntity.ok("Configuración de privacidad actualizada correctamente");
    }
}