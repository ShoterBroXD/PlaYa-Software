package com.playa.controller;

import com.playa.dto.CommentResponseDto;
import com.playa.dto.CommunityResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.playa.service.CommentService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // POST /api/v1/comments - Crear comentario
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentResponseDto commentRequestDto) {
        try {
            CommentResponseDto createdComment = commentService.createComment(commentRequestDto);
            return ResponseEntity.status(201).body(createdComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/v1/comments/{id} - Obtener comentario
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
        Optional<CommentResponseDto> comment = commentService.getCommentById(id);
         return comment.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/v1/comments/{id} - Eliminar comentario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
