package com.playa.service;

import com.playa.dto.PostRequestDto;
import com.playa.dto.PostResponseDto;
import com.playa.exception.ResourceNotFoundException;
import com.playa.model.Post;
import com.playa.repository.ThreadRepository;
import org.springframework.stereotype.Service;
import com.playa.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private PostRepository postRepository;

    private ThreadRepository threadRepository;

    // Métodos de lógica de negocio para posts

    @Transactional
    public PostResponseDto createPost(PostRequestDto dto) {
        if (dto.getIdPost() == null || dto.getIdThread() == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Los campos idPost, idThread y content son obligatorios");
        }
        if (!threadRepository.existsById(dto.getIdThread())) {
            throw new IllegalArgumentException("El hilo con ID " + dto.getIdThread() + " no existe");
        }
        if (!postRepository.existsById(dto.getIdPost())) {
            throw new IllegalArgumentException("El post con ID " + dto.getIdPost() + " no existe");
        }
        Post post = new Post();
        post.setIdPost(dto.getIdPost());
        post.setIdThread(dto.getIdThread());
        post.setContent(dto.getContent().trim());
        Post saved = postRepository.save(post);
        return toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        return postRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(()-> new ResourceNotFoundException("El post no existe."));
    }

    @Transactional
    public boolean deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("El post no existe."));
        postRepository.delete(post);
        return false;
    }

    private PostResponseDto toResponseDto(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setIdPost(post.getIdPost());
        dto.setIdThread(post.getIdThread());
        dto.setContent(post.getContent());
        return dto;
    }
}
