package com.tralaleros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tralaleros.repository.CommunityRepository;
import com.tralaleros.model.Community;

@Service
public class CommunityService {
    
    @Autowired
    private CommunityRepository communityRepository;
    
    // Métodos de lógica de negocio para comunidades
}