package com.playa.service;

import com.playa.dto.CommunityResponseDto;
import com.playa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playa.repository.CommentRepository;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;
    
    // Métodos de lógica de negocio para comentarios
    public CommunityResponseDto createCommunity(CommunityResponseDto communityRequestDto) {
        // Lógica para crear una comunidad
        return null; // Reemplazar con la comunidad creada
    }
}