package com.tralaleros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tralaleros.repository.UserRepository;
import com.tralaleros.model.User;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        // Establecer fecha de registro automáticamente
        if (user.getRegisterDate() == null) {
            user.setRegisterDate(LocalDateTime.now());
        }
        // Establecer premium como false por defecto si no se especifica
        if (user.getPremium() == null) {
            user.setPremium(false);
        }
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Usuario no encontrado con id: " + id)
        );

        // Actualizar solo los campos que no son null
        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getBiography() != null) {
            user.setBiography(userDetails.getBiography());
        }
        if (userDetails.getPremium() != null) {
            user.setPremium(userDetails.getPremium());
        }
        if (userDetails.getRedSocial() != null) {
            user.setRedSocial(userDetails.getRedSocial());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
    }
}