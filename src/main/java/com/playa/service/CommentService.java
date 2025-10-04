package com.playa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playa.repository.CommentRepository;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    // Métodos de lógica de negocio para comentarios
}