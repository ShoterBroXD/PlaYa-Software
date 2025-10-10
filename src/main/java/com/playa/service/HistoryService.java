package com.playa.service;

import com.playa.dto.HistoryRequestDto;
import com.playa.dto.HistoryResponseDto;
import com.playa.model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playa.repository.HistoryRepository;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    // Métodos de lógica de negocio para historial
    public HistoryResponseDto createHistory(HistoryRequestDto historyRequestDto) {
//        History history = new History();
//        history.setUser(historyRequestDto.getIdUser());
//        history.setSong(historyRequestDto.getIdSong());
//        History savedHistory = historyRepository.save(history);
//        return new HistoryResponseDto(savedHistory.getUser(), savedHistory.getSong());
    }


    public HistoryResponseDto getHistoryByUserId(Long idUser) {

        return null; // Reemplazar con la implementación real
    }
}
