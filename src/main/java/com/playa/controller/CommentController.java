package com.playa.controller;

import com.playa.service.CommentService;
import com.playa.dto.CommentRequestDto;
import com.playa.dto.CommentResponseDto;
import com.playa.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // GET /api/v1/comments - Obtener todos los comentarios
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getAllComments() {
        List<CommentResponseDto> comments = commentService.getAllComments();
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(comments); // 200
    }

    // POST /api/v1/comments - Crear comentario
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto) {
        CommentResponseDto response = commentService.createComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/v1/comments/{id} - Obtener comentario
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long id) {
        try {
            CommentResponseDto response = commentService.getComment(id);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 es correcto aquí - recurso específico no encontrado
        }
    }

    // GET /api/v1/comments/song/{songId} - Obtener comentarios de una canción
    @GetMapping("/song/{songId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsBySong(@PathVariable Long songId) {
        List<CommentResponseDto> comments = commentService.getCommentsBySong(songId);
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(comments); // 200
    }

    // POST /api/v1/comments/{id}/report - Reportar/ocultar comentario
    @PostMapping("/{id}/report")
    public ResponseEntity<String> reportComment(@PathVariable Long id) {
        commentService.reportComment(id);
        return ResponseEntity.ok("Comentario reportado y ocultado exitosamente");
    }

    // POST /api/v1/comments/{id}/unreport - Mostrar comentario reportado
    @PostMapping("/{id}/unreport")
    public ResponseEntity<String> unreportComment(@PathVariable Long id) {
        commentService.unreportComment(id);
        return ResponseEntity.ok("Comentario habilitado exitosamente");
    }

    // DELETE /api/v1/comments/{id} - Eliminar comentario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
