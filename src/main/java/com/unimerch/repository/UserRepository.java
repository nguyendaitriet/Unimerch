package com.unimerch.repository;

import com.unimerch.dto.UserCreateParam;
import com.unimerch.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User getByUsername(String username);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
