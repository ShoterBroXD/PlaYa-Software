package com.playa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.playa.service.CommentService;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // POST /api/v1/comments - Crear comentario
    // GET /api/v1/comments/{id} - Obtener comentario
    // DELETE /api/v1/comments/{id} - Eliminar comentario
}
