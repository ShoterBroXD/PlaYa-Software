package com.playa.service;

import com.playa.dto.UserRequestDto;
import com.playa.dto.UserResponseDto;
import com.playa.mapper.UserMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playa.repository.UserRepository;
import com.playa.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;



    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = userMapper.convertToEntity(userRequestDto);
        user.setRegisterDate(LocalDateTime.now());
        user.setPremium(false);
        User savedUser = userRepository.save(user);
        return userMapper.convertToResponseDto(savedUser);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
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

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
    }



}