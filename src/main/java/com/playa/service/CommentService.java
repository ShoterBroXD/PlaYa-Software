package com.playa.service;

import com.playa.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playa.repository.CommentRepository;
import com.playa.dto.CommentRequestDto;
import com.playa.dto.CommentResponseDto;
import com.playa.model.Comment;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import com.playa.repository.UserRepository;
import com.playa.repository.SongRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto dto) {
        // Validar que los campos requeridos no sean nulos
        if (dto.getIdUser() == null || dto.getIdSong() == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Los campos idUser, idSong y content son obligatorios");
        }

        // Validar que el usuario existe
        if (!userRepository.existsById(dto.getIdUser())) {
            throw new ResourceNotFoundException("El usuario con ID " + dto.getIdUser() + " no existe");
        }

        // Validar que la canción existe
        if (!songRepository.existsById(dto.getIdSong())) {
            throw new ResourceNotFoundException("La canción con ID " + dto.getIdSong() + " no existe");
        }

        // Validar el comentario padre si existe
        if (dto.getParentComment() != null && !commentRepository.existsById(dto.getParentComment())) {
            throw new ResourceNotFoundException("El comentario padre con ID " + dto.getParentComment() + " no existe");
        }

        Comment comment = new Comment();
        comment.setIdUser(dto.getIdUser());
        comment.setIdSong(dto.getIdSong());
        comment.setContent(dto.getContent().trim());
        comment.setParentComment(dto.getParentComment());
        comment.setDate(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public CommentResponseDto getComment(Long id) {
        return commentRepository.findById(id)
            .map(this::toResponseDto)
                .orElseThrow(()-> new ResourceNotFoundException("El comentario no existe"));
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("El comentario no existe"));
        commentRepository.delete(comment);


    }

    private CommentResponseDto toResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setIdComment(comment.getIdComment());
        dto.setIdUser(comment.getIdUser());
        dto.setIdSong(comment.getIdSong());
        dto.setContent(comment.getContent());
        dto.setParentComment(comment.getParentComment());
        dto.setDate(comment.getDate());
        return dto;
    }
}