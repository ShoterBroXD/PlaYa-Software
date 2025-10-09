package com.playa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.playa.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // POST /api/v1/posts - Crear post en hilo
    // GET /api/v1/posts/{id} - Obtener post
}
