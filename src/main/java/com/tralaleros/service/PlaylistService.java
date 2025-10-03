package com.tralaleros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tralaleros.repository.PlaylistRepository;
import com.tralaleros.model.Playlist;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    // Métodos de lógica de negocio para playlists
}
