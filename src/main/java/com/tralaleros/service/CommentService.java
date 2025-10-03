package com.tralaleros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tralaleros.repository.CommentRepository;
import com.tralaleros.model.Comment;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    // Métodos de lógica de negocio para comentarios
}