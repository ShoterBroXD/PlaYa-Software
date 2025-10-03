package com.tralaleros.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "community")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_community")
    private Long idCommunity;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, name = "creationdate")
    private LocalDateTime creationDate;

    // Constructores
    public Community() {}

    // Getters y Setters
    public Long getIdCommunity() { return idCommunity; }
    public void setIdCommunity(Long idCommunity) { this.idCommunity = idCommunity; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
}
