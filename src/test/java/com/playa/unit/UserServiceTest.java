package com.playa.unit;

import com.playa.dto.UserResponseDto;
import com.playa.mapper.UserMapper;
import com.playa.model.User;
import com.playa.model.enums.Rol;
import com.playa.repository.GenreRepository;
import com.playa.repository.UserRepository;
import com.playa.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Pruebas Unitarias - US-009: Filtrar artistas por género musical")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<User> criteriaQuery;

    @Mock
    private Root<User> root;

    @Mock
    private TypedQuery<User> typedQuery;

    @InjectMocks
    private UserService userService;

    // Artistas para los tests
    private User artistRock;
    private User artistRock2;

    // DTOs de respuesta
    private UserResponseDto artistRockDto;
    private UserResponseDto artistRock2Dto;

    @BeforeEach
    void setUp() {
        // Inyectar manualmente el EntityManager mock porque usa @PersistenceContext
        ReflectionTestUtils.setField(userService, "entityManager", entityManager);

        artistRock = User.builder()
                .idUser(1L)
                .name("Artista Rock")
                .email("rock@example.com")
                .password("password123")
                .type(Rol.ARTIST)
                .idgenre(1L) // Rock
                .registerDate(LocalDateTime.now())
                .premium(false)
                .biography("Especialista en rock")
                .active(true)
                .build();

        artistRock2 = User.builder()
                .idUser(2L)
                .name("Segundo Artista Rock")
                .email("rock2@example.com")
                .password("password123")
                .type(Rol.ARTIST)
                .idgenre(1L) // Rock
                .registerDate(LocalDateTime.now())
                .premium(false)
                .biography("También especialista en rock")
                .active(true)
                .build();

        artistRockDto = UserResponseDto.builder()
                .idUser(1L)
                .name("Artista Rock")
                .email("rock@example.com")
                .type(Rol.ARTIST)
                .registerDate(artistRock.getRegisterDate())
                .biography("Especialista en rock")
                .premium(false)
                .build();

        artistRock2Dto = UserResponseDto.builder()
                .idUser(2L)
                .name("Segundo Artista Rock")
                .email("rock2@example.com")
                .type(Rol.ARTIST)
                .registerDate(artistRock2.getRegisterDate())
                .biography("También especialista en rock")
                .premium(false)
                .build();
    }

    @Test
    @DisplayName("US-009 - Escenario 01: Debe mostrar únicamente artistas del género seleccionado (Rock)")
    void filterArtists_whenSelectRockGenre_returnsOnlyRockArtists() {
        Long genreIdRock = 1L;
        List<User> rockArtists = Arrays.asList(artistRock, artistRock2); // 2 artistas de rock

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(User.class)).thenReturn(root);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(rockArtists);

        when(userMapper.convertToResponseDto(artistRock)).thenReturn(artistRockDto);
        when(userMapper.convertToResponseDto(artistRock2)).thenReturn(artistRock2Dto);

        // When aplico el filtro por género Rock
        List<UserResponseDto> result = userService.filterArtists(Rol.ARTIST, null, genreIdRock);

        // Then se muestran únicamente artistas de ese género (Rock)
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals(2, result.size(), "Debe retornar exactamente 2 artistas de rock");

        // Verificar que todos son de tipo ARTIST
        assertTrue(result.stream().allMatch(dto -> dto.getType() == Rol.ARTIST),
                "Todos deben ser artistas");

        // Verificar que ambos artistas son de Rock
        assertTrue(result.stream().anyMatch(dto -> dto.getName().equals("Artista Rock")),
                "Debe incluir al primer artista de rock");
        assertTrue(result.stream().anyMatch(dto -> dto.getName().equals("Segundo Artista Rock")),
                "Debe incluir al segundo artista de rock");

        verify(entityManager, times(1)).getCriteriaBuilder();
        verify(userMapper, times(2)).convertToResponseDto(any(User.class));
    }

    @Test
    @DisplayName("US-009 - Escenario 02: Debe mostrar mensaje cuando el género no tiene artistas")
    void filterArtists_whenGenreHasNoArtists_returnsEmptyList() {
        // Given que el usuario selecciona un género inexistente o sin artistas (ID: 999)
        Long nonExistentGenreId = 999L;
        List<User> emptyList = Collections.emptyList();

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(User.class)).thenReturn(root);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(emptyList);

        List<UserResponseDto> result = userService.filterArtists(Rol.ARTIST, null, nonExistentGenreId);

        // Then aparece el mensaje "No se encontraron resultados" (lista vacía)
        assertNotNull(result, "El resultado debe ser una lista, no null");
        assertTrue(result.isEmpty(),
                "La lista debe estar vacía cuando no hay artistas del género (equivalente a 'No se encontraron resultados')");

        verify(entityManager, times(1)).getCriteriaBuilder();
        verify(userMapper, never()).convertToResponseDto(any(User.class));
    }
}

