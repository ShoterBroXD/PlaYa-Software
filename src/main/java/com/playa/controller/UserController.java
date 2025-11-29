package com.playa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.playa.service.UserService;
import com.playa.dto.UserRequestDto;
import com.playa.dto.UserResponseDto;
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

    // GET /api/v1/users/nuevos - Obtener artistas nuevos
    @GetMapping("/nuevos")
    public ResponseEntity<List<UserResponseDto>> getNewArtists(@RequestParam(required = false, defaultValue = "10") int limit) {
        // Note: limit is currently ignored by service but added to match frontend signature
        List<UserResponseDto> newArtists = userService.getNewArtists();
        return ResponseEntity.ok(newArtists);
    }

    // GET /api/v1/users/artists/filter - Filtrar artistas
    @GetMapping("/artists/filter")
    public ResponseEntity<List<UserResponseDto>> filterArtists(
            @RequestParam(required = false) com.playa.model.enums.Rol role,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long idgenre) {
        List<UserResponseDto> artists = userService.filterArtists(role, name, idgenre);
        return ResponseEntity.ok(artists);
    }

    // GET /api/v1/users/genre/{idGenre} - Obtener artistas por género
    @GetMapping("/genre/{idGenre}")
    public ResponseEntity<List<UserResponseDto>> getArtistsByGenre(@PathVariable Long idGenre) {
        List<UserResponseDto> artists = userService.findAllByIdGenre(idGenre);
        return ResponseEntity.ok(artists);
    }
}