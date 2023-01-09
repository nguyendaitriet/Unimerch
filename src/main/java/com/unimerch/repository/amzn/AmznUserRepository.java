package com.unimerch.repository.amzn;

import com.unimerch.dto.amznacc.AmznAccParam;
import com.unimerch.repository.model.amzn_user.AmznUser;
import com.unimerch.repository.model.amzn_user.AzmnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Modifying
    @Query("UPDATE AmznUser a " +
            "SET a.lastCheck = :currentTime " +
            "WHERE a.id = :id")
    void updateLastCheck(@Param("id") Integer id, @Param("currentTime") Instant currentTime);

    @Query("SELECT a.username " +
            "FROM AmznUser AS a")
    List<String> getAllAmznUsername();
}
