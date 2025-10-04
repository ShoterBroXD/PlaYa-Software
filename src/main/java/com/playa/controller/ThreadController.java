package com.playa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.playa.service.ThreadService;

@RestController
@RequestMapping("/api/v1/threads")
public class ThreadController {

    @Autowired
    private ThreadService threadService;

    // POST /api/v1/threads - Crear hilo en comunidad
    // GET /api/v1/threads/{id} - Ver hilo
}
