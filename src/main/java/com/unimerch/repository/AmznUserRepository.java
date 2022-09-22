package com.unimerch.repository;

import com.unimerch.repository.model.AmznUser;
import com.unimerch.repository.model.AzmnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AmznUserRepository extends JpaRepository<AmznUser, Integer> {

    boolean existsByUsername(String username);

    AmznUser findByUsername(String username);

    List<AmznUser> findByStatus(AzmnStatus status);

    void deleteAllByIdIn(List<Integer> ids);

    List<AmznUser> findAllByLastCheckBefore(Instant lastCheck);
}
