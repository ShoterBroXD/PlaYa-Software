package com.tralaleros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tralaleros.service.SongService;

@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    @Autowired
    private SongService songService;

    // POST /api/v1/songs - Subir canción
    // GET /api/v1/songs/{id} - Consultar detalles canción
    // PUT /api/v1/songs/{id} - Actualizar canción
    // DELETE /api/v1/songs/{id} - Eliminar canción
}
