package com.playa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playa.repository.CommunityRepository;

@Service
public class CommunityService {
    
    @Autowired
    private CommunityRepository communityRepository;
    
    // Métodos de lógica de negocio para comunidades
}