package com.unimerch.repository.group;

import com.unimerch.repository.model.AmznUser;
import com.unimerch.repository.model.BrgGroupAmznUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BrgGroupAmznUserRepository extends JpaRepository<BrgGroupAmznUser, Integer> {

    @Query("SELECT a " +
            "FROM AmznUser AS a " +
            "WHERE a.id NOT IN ( " +
                "SELECT br.amznUser.id " +
                "FROM BrgGroupAmznUser AS br " +
                "WHERE br.group.id = :id" +
            ")"
    )
    List<AmznUser> getAmznAccOutGroup(@Param("id") Integer id);

    @Query("SELECT a " +
            "FROM BrgGroupAmznUser AS br " +
            "INNER JOIN AmznUser AS a " +
            "ON br.amznUser.id = a.id " +
            "WHERE br.group.id = :id ")
    List<AmznUser> getAmznAccInGroup(@Param("id") Integer id);

    @Query("SELECT br.amznUser.id " +
            "FROM BrgGroupAmznUser AS br " +
            "WHERE br.group.id = :id ")
    List<Integer> getAmznAccIdInGroup(@Param("id") Integer id);

    @Query("DELETE FROM BrgGroupAmznUser AS br " +
            "WHERE (br.group.id = :groupId AND br.amznUser.id = :amznAccId) ")
    @Modifying
    void deleteAmznAccFromGroup(@Param("amznAccId") Integer amznAccId, @Param("groupId") Integer groupId);

    void deleteByAmznUserId(Integer amznAccId);

    void deleteAllByAmznUserIdIn(List<Integer> ids);

    void deleteAllByGroupIdAndAmznUserIdIn(Integer groupId, List<Integer> amznAccIds);

    @Query("SELECT a " +
            "FROM BrgGroupAmznUser AS br " +
            "INNER JOIN AmznUser AS a " +
            "ON br.amznUser.id = a.id " +
            "WHERE br.group.id = :id " +
            "AND a.status = 'TERMINATED'")
    List<AmznUser> getAmznAccDieInGroup(@Param("id") Integer id);

    @Query("SELECT a " +
            "FROM BrgGroupAmznUser AS br " +
            "INNER JOIN AmznUser AS a " +
            "ON br.amznUser.id = a.id " +
            "WHERE br.group.id = :groupId " +
            "AND a.lastCheck < :lastCheck")
    List<AmznUser> findAllByLastCheckBeforeAndGroupId(Instant lastCheck, Integer groupId);
}
