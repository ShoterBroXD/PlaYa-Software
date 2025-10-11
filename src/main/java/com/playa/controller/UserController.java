package com.playa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.playa.service.UserService;
import com.playa.dto.UserRequestDto;
import com.playa.dto.UserResponseDto;
import com.playa.dto.UserPreferencesDto;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/v1/users - Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(users); // 200
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
    public ResponseEntity<?> resetPreferences(@PathVariable Long id) {
        userService.resetUserPreferences(id);
        return ResponseEntity.ok("Preferencias reiniciadas. Recibirás recomendaciones desde cero");
    }
}