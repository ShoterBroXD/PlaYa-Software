package com.tralaleros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tralaleros.service.PostService;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // POST /api/v1/posts - Crear post en hilo
    // GET /api/v1/posts/{id} - Obtener post
}
