package com.tralaleros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tralaleros.repository.PostRepository;
import com.tralaleros.model.Post;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // Métodos de lógica de negocio para posts
}
