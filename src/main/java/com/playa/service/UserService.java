package com.playa.service;

import com.playa.dto.PasswordChangeRequestDto;
import com.playa.dto.PasswordResetRequestDto;
import com.playa.dto.UserRequestDto;
import com.playa.dto.UserResponseDto;
import com.playa.mapper.UserMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.playa.repository.UserRepository;
import com.playa.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::convertToResponseDto)
                .collect(Collectors.toList());
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
    public Optional<UserResponseDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::convertToResponseDto);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userDetails) {
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
        if (userDetails.getRedSocial() != null) {
            user.setRedSocial(userDetails.getRedSocial());
        }

        User savedUser = userRepository.save(user);
        return userMapper.convertToResponseDto(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void changePassword(Long idUser, PasswordChangeRequestDto request){
        User user=userRepository.findById(idUser)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        if(!user.getPassword().equals(request.getCurrentPassword())){
            throw new RuntimeException("Credenciales no coinciden intentelo de nuevo");
        }
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())){
            throw new RuntimeException("La contraseña de este campo debe de coincidir con la nueva contraseña");
        }
        //user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    @Transactional
    public void resetPassword(PasswordResetRequestDto request){
        User user=userRepository.findByEmail(request.getEmail());
        if (user == null){
            throw new RuntimeException("Usuario no encontrado");
        }
        if(!user.getEmail().equals(request.getEmail())){
            throw new RuntimeException("Email no registrado para el usuario, asegurese que el email introducido sea el ligado a su cuenta");
        }
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())){
            throw new RuntimeException("La contraseña de este campo debe de coincidir con la nueva contraseña");
        }
        //user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
}