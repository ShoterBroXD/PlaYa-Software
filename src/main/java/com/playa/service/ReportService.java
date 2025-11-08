package com.playa.service;

import com.playa.exception.ResourceNotFoundException;
import com.playa.model.Song;
import com.playa.model.User;
import com.playa.model.Comment;
import com.playa.repository.SongRepository;
import com.playa.repository.UserRepository;
import com.playa.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    public String reportSong(Long songId, Long userId, String reason) {
        // Validar motivo del reporte
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes indicar el motivo del reporte");
        }

        // Validar que la canción existe
        Optional<Song> songOptional = songRepository.findById(songId);
        if (songOptional.isEmpty()) {
            throw new ResourceNotFoundException("Canción no encontrada con ID: " + songId);
        }

        // Validar que el usuario existe
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + userId);
        }

        Song song = songOptional.get();
        User user = userOptional.get();

        // Validar que el usuario no está reportando su propia canción
        if (song.getUser().getIdUser().equals(userId)) {
            throw new IllegalArgumentException("No puedes reportar tu propia canción");
        }

        // Aquí iría la lógica para guardar el reporte en la base de datos
        // Por ahora solo retornamos un mensaje de confirmación
        return "Reporte enviado correctamente. Gracias por tu colaboración.";
    }

    public Song validateSongExists(Long songId) {
        Optional<Song> songOptional = songRepository.findById(songId);
        if (songOptional.isEmpty()) {
            throw new ResourceNotFoundException("Canción no encontrada con ID: " + songId);
        }
        return songOptional.get();
    }

    public User validateUserExists(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + userId);
        }
        return userOptional.get();
    }

    public Long countUserSongs(Long userId) {
        User user = validateUserExists(userId);
        return songRepository.countByUserAndVisibilityNot(user, "deleted");
    }

    public List<Comment> getSongComments(Long songId) {
        validateSongExists(songId);
        return commentRepository.findBySong_IdSongOrderByDateDesc(songId);
    }

    public boolean isValidReportReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            return false;
        }

        String[] validReasons = {
            "Discurso de odio",
            "Contenido sexual",
            "Violencia",
            "Spam",
            "Derechos de autor"
        };

        for (String validReason : validReasons) {
            if (validReason.equals(reason)) {
                return true;
            }
        }
        return false;
    }

    public String createReportStructure(Long userId, Long songId, String reason) {
        validateUserExists(userId);
        validateSongExists(songId);

        if (!isValidReportReason(reason)) {
            throw new IllegalArgumentException("Motivo de reporte no válido");
        }

        return "Reporte creado exitosamente para canción " + songId + " por usuario " + userId + " con motivo: " + reason;
    }
}
