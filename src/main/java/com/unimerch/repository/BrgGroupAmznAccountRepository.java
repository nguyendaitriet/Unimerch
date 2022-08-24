package com.unimerch.repository;

import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.repository.model.BrgGroupAmznAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrgGroupAmznAccountRepository extends JpaRepository<BrgGroupAmznAccount, Integer> {

    @Query("SELECT a " +
            "FROM AmznAccount AS a " +
            "WHERE a.id NOT IN ( " +
                "SELECT br.amznAccount.id " +
                "FROM BrgGroupAmznAccount AS br " +
                "INNER JOIN AmznAccount AS a " +
                "ON br.amznAccount.id = a.id " +
                "WHERE br.group.id = :id) ")
    List<AmznAccount> getAmznAccOutGroup(@Param("id") Integer id);

    @Query("SELECT a " +
            "FROM BrgGroupAmznAccount AS br " +
            "INNER JOIN AmznAccount AS a " +
            "ON br.amznAccount.id = a.id " +
            "WHERE br.group.id = :id ")
    List<AmznAccount> getAmznAccInGroup(@Param("id") Integer id);

    @Query("DELETE FROM BrgGroupAmznAccount AS br " +
            "WHERE (br.group.id = :groupId AND br.amznAccount.id = :amznAccId) ")
    @Modifying
    void deleteAmznAccFromGroup(@Param("amznAccId") Integer amznAccId, @Param("groupId") Integer groupId);

}
