package com.playa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

import java.util.HashSet;

@Entity
@Table(name = "genre")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idgenre")
    private Long idGenre;

    @Column(nullable = true, unique = true, length = 50)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Song> songs = new HashSet<>();
}
