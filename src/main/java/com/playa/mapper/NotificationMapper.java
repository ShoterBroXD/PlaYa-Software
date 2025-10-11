package com.playa.mapper;

import com.playa.dto.NotificationRequestDto;
import com.playa.dto.NotificationResponseDto;
import com.playa.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification convertToEntity(NotificationRequestDto requestDto) {
        return Notification.builder()
                .idUser(requestDto.getIdUser())
                .content(requestDto.getContent())
                .date(requestDto.getDate())
                .build();
    }

    public NotificationResponseDto convertToResponseDto(Notification notification) {
        return NotificationResponseDto.builder()
                .idNotification(notification.getIdNotification())
                .idUser(notification.getIdUser())
                .content(notification.getContent())
                .read(notification.getRead())
                .date(notification.getDate())
                .build();
    }
}
