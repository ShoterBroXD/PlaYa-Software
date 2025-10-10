package com.playa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.playa.repository.CommunityRepository;
import com.playa.repository.CommunityUserRepository;
import com.playa.repository.UserRepository;
import com.playa.model.Community;
import com.playa.model.CommunityUser;
import com.playa.model.User;
import com.playa.dto.CommunityRequestDto;
import com.playa.dto.CommunityResponseDto;
import com.playa.dto.UserResponseDto;
import com.playa.dto.JoinCommunityDto;
import com.playa.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CommunityService {
    
    @Autowired
    private CommunityRepository communityRepository;
    
    @Autowired
    private CommunityUserRepository communityUserRepository;

    @Autowired
    private UserRepository userRepository;

    // Crear nueva comunidad
    public CommunityResponseDto createCommunity(CommunityRequestDto communityRequestDto) {
        Community community = new Community();
        community.setName(communityRequestDto.getName());
        community.setDescription(communityRequestDto.getDescription());
        community.setCreationDate(LocalDateTime.now());

        Community savedCommunity = communityRepository.save(community);
        return convertToResponseDto(savedCommunity, null);
    }

    // Obtener comunidad por ID
    public Optional<CommunityResponseDto> getCommunityById(Long id) {
        Optional<Community> communityOpt = communityRepository.findById(id);
        if (communityOpt.isPresent()) {
            Community community = communityOpt.get();
            return Optional.of(convertToResponseDto(community, null));
        }
        return Optional.empty();
    }

    // Unirse a una comunidad
    @Transactional
    public void joinCommunity(Long communityId, JoinCommunityDto joinDto) {
        // Verificar que la comunidad existe
        Community community = communityRepository.findById(communityId).orElseThrow(
            () -> new ResourceNotFoundException("Comunidad no encontrada con id: " + communityId)
        );

        // Verificar que el usuario existe
        User user = userRepository.findById(joinDto.getIdUser()).orElseThrow(
            () -> new ResourceNotFoundException("Usuario no encontrado con id: " + joinDto.getIdUser())
        );

        // Verificar si el usuario ya es miembro de la comunidad
        if (communityUserRepository.existsByIdCommunityAndIdUser(communityId, joinDto.getIdUser())) {
            throw new RuntimeException("El usuario ya es miembro de esta comunidad");
        }

        // Agregar el usuario a la comunidad
        CommunityUser communityUser = new CommunityUser(communityId, joinDto.getIdUser());
        communityUserRepository.save(communityUser);
    }

    // Obtener miembros de una comunidad
    public List<UserResponseDto> getCommunityMembers(Long communityId) {
        // Verificar que la comunidad existe
        communityRepository.findById(communityId).orElseThrow(
            () -> new ResourceNotFoundException("Comunidad no encontrada con id: " + communityId)
        );

        List<User> users = communityUserRepository.findUsersByCommunityId(communityId);
        return users.stream()
                .map(this::convertUserToResponseDto)
                .collect(Collectors.toList());
    }

    // Obtener todas las comunidades
    public List<CommunityResponseDto> getAllCommunities() {
        return communityRepository.findAllByOrderByCreationDateDesc().stream()
                .map(community -> convertToResponseDto(community, null))
                .collect(Collectors.toList());
    }

    // Buscar comunidades por nombre
    public List<CommunityResponseDto> searchCommunitiesByName(String name) {
        return communityRepository.findByNameContainingIgnoreCase(name).stream()
                .map(community -> convertToResponseDto(community, null))
                .collect(Collectors.toList());
    }

    // Método auxiliar para convertir Community a CommunityResponseDto
    private CommunityResponseDto convertToResponseDto(Community community, List<UserResponseDto> members) {
        CommunityResponseDto dto = new CommunityResponseDto(
            community.getIdCommunity(),
            community.getName(),
            community.getDescription(),
            community.getCreationDate()
        );
        if (members != null) {
            dto.setMembers(members);
        }
        return dto;
    }

    // Método auxiliar para convertir User a UserResponseDto
    private UserResponseDto convertUserToResponseDto(User user) {
        return new UserResponseDto(
            user.getIdUser(),
            user.getName(),
            user.getEmail(),
            user.getType(),
            user.getRegisterDate(),
            user.getBiography(),
            user.getPremium(),
            user.getRedSocial()
        );
    }
}