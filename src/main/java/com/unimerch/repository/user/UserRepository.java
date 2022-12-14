package com.unimerch.repository.user;

import com.unimerch.repository.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsById(int id);

    @Modifying
    @Transactional
    @Query("UPDATE User u " +
            "SET u.passwordHash = :newPass " +
            "WHERE u.id = :id")
    void changePassword(@Param("id") int id, @Param("newPass") String passwordHash);
}
