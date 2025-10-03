package com.tralaleros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tralaleros.repository.LikeRepository;
import com.tralaleros.model.Like;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    // Métodos de lógica de negocio para likes
}
