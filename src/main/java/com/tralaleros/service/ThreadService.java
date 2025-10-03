package com.tralaleros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tralaleros.repository.ThreadRepository;
import com.tralaleros.model.Thread;

@Service
public class ThreadService {

    @Autowired
    private ThreadRepository threadRepository;

    // Métodos de lógica de negocio para hilos
}
