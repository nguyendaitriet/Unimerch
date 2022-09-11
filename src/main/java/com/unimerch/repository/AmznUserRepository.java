package com.unimerch.repository;

import com.unimerch.repository.model.AmznUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmznUserRepository extends JpaRepository<AmznUser, Integer> {

    boolean existsByUsername(String username);

    AmznUser findByUsername(String username);
}
