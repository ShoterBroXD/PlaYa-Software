package com.tralaleros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tralaleros.repository.HistoryRepository;
import com.tralaleros.model.History;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    // Métodos de lógica de negocio para historial
}
