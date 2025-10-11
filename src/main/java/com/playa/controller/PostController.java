package com.playa.controller;

import com.playa.dto.PostRequestDto;
import com.playa.dto.PostResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.playa.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService postService;

    // POST /api/v1/posts - Crear post en hilo
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto dto) {
        PostResponseDto response = postService.createPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // GET /api/v1/posts/{id} - Obtener post
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto response = postService.getPost(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(response);
    }
    // DELETE /api/v1/posts/{id} - Eliminar post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }
}
