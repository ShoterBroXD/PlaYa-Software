package com.tralaleros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tralaleros.service.HistoryService;

@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    // POST /api/v1/history - Registrar reproducción
    // GET /api/v1/history/{idUser} - Historial de usuario
}
