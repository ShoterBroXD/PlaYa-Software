package com.playa.service;

import com.playa.dto.UserRequestDto;
import com.playa.dto.UserResponseDto;
import com.playa.mapper.UserMapper;
import com.playa.model.enums.Rol;
import com.playa.repository.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.playa.repository.UserRepository;
import com.playa.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final UserMapper userMapper;

    //private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        user.setName(userDetails.getName());
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

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<UserResponseDto> filterArtists(Rol role, String name, Long idgenre) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (role != null) {
            predicates.add(cb.equal(user.get("type"), role));
        }

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(cb.lower(user.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (idgenre != null) {
            predicates.add(cb.equal(user.get("idgenre"), idgenre));
        }

        cq.select(user).where(predicates.toArray(new Predicate[0]));

        List<User> users = entityManager.createQuery(cq).getResultList();

        return users.stream()
                .map(userMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }
    @Transactional

    public List<UserResponseDto> findAllByIdGenre(Long idGenre) {
        return userRepository.findAllByIdgenre(idGenre)
                .stream().map(userMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserPreferences(Long id, List<String> genres) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        user.setFavoriteGenres(genres);
        userRepository.save(user);
    }

    @Transactional
    public void resetUserPreferences(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        user.setIdgenre(null); // Limpia género principal
        user.setFavoriteGenres(new java.util.ArrayList<>()); // Limpia lista de géneros favoritos
        // TODO: Limpiar historial y likes si corresponde (historyRepository, likeRepository) cuando existan métodos.
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getNewArtists(){
        LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);
        return userRepository.findNewArtists(fourteenDaysAgo).stream()
                .map(userMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }
}