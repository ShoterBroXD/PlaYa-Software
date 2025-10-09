package com.playa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.playa.service.LikeService;

@RestController
@RequestMapping("/songs")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // POST /api/v1/songs/{id}/like - Dar like a canción
    // DELETE /api/v1/songs/{id}/like - Quitar like
}
