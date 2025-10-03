package com.tralaleros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tralaleros.service.LikeService;

@RestController
@RequestMapping("/api/v1/songs")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // POST /api/v1/songs/{id}/like - Dar like a canción
    // DELETE /api/v1/songs/{id}/like - Quitar like
}
