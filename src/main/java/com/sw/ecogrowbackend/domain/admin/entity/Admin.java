package com.sw.ecogrowbackend.domain.admin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String adminToken;

    public Admin(String username, String password, String name, String email, String adminToken) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.adminToken = adminToken;
    }
}