package com.playa.service;

import com.playa.dto.request.GenreRequestDTO;
import com.playa.dto.response.GenreResponseDTO;
import com.playa.exception.BusinessRuleException;
import com.playa.model.Genre;
import com.playa.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    @Transactional
    public GenreResponseDTO createGenre(GenreRequestDTO dto) {
        if (genreRepository.findByName(dto.name()).isPresent()){
            throw new BusinessRuleException("El generon ya existe");
        }

        Genre g= new Genre();
        g.setName(dto.name());

        Genre saved = genreRepository.save(g);
        return toDto(saved);
    }

    public Genre createGenre(String name){
        Genre g= new Genre();
        g.setName(name);
        return genreRepository.save(g);
    }

    public List<Genre> getAllGenres(){
        return genreRepository.findAll();
    }
}
