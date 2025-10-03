package com.tralaleros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tralaleros.service.CommunityService;

@RestController
@RequestMapping("/api/v1/communities")
public class CommunityController {
    
    @Autowired
    private CommunityService communityService;
    
    // POST /api/v1/communities - Crear comunidad
    // GET /api/v1/communities/{id} - Ver comunidad
    // POST /api/v1/communities/{id}/users - Unirse a comunidad
    // GET /api/v1/communities/{id}/users - Listar miembros
}