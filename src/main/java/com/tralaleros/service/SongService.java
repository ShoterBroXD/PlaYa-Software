package com.tralaleros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tralaleros.repository.SongRepository;
import com.tralaleros.model.Song;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    // Métodos de lógica de negocio para canciones
}
