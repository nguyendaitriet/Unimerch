package com.unimerch.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 128)
    private String username;

    @Column(name = "full_name", nullable = false, length = 80)
    private String fullName;

    @Column(name = "mobile", length = 15)
    private String mobile;

    @Column(name = "password_hash", nullable = false, length = 128)
    private String passwordHash;

    @Column(name = "salt", nullable = false, length = 32)
    private String salt;

    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


}