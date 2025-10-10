package com.playa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "\"like\"")
public class Like {

    @EmbeddedId
    private LikeId id;

    @Column(nullable = false)
    private LocalDateTime date;

    public Like() {}

    public Like(LikeId id, LocalDateTime date) {
        this.id = id;
        this.date = date;
    }
}

