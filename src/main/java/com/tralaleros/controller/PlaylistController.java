package com.tralaleros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tralaleros.service.PlaylistService;

@RestController
@RequestMapping("/api/v1/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    // POST /api/v1/playlists - Crear playlist
    // GET /api/v1/playlists/{id} - Obtener playlist
    // POST /api/v1/playlists/{id}/songs - Agregar canción a playlist
    // DELETE /api/v1/playlists/{id}/songs/{songId} - Quitar canción de playlist
}
