package com.playa.model;

import com.playa.model.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser")
    private Long idUser;

    @Column(length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    // @Column(nullable = false, length = 20)
    //private String type; // 'artist', 'listener', 'admin' // Podría ser un ENUM

    @Enumerated(EnumType.STRING)
    private Rol type;

    @Column(nullable = false, name = "registerdate")
    private LocalDateTime registerDate;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @Column(nullable = false)
    private Boolean premium;

    @Column(name = "redsocial", columnDefinition = "TEXT")
    private String redSocial;

    @Column(name="idgenre")
    private String idgenre;

    public User(String name, String email, String password, Rol type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
        this.premium = false;
        this.registerDate = LocalDateTime.now();
    }

}