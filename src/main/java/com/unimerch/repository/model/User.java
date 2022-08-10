package com.unimerch.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Accessors(chain = true)
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

    @Column(name = "password_hash", nullable = false, length = 128, updatable = false)
    private String passwordHash;

    @Column(name = "salt", nullable = false, length = 32, updatable = false)
    private String salt;

    @Column(name = "registered_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant registeredAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


}