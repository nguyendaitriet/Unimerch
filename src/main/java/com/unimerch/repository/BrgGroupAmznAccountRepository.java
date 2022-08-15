package com.unimerch.repository;

import com.unimerch.dto.AmznAccAddedToGroup;
import com.unimerch.repository.model.BrgGroupAmznAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrgGroupAmznAccountRepository extends JpaRepository<BrgGroupAmznAccount, Integer> {

    @Query("SELECT NEW com.unimerch.dto.AmznAccAddedToGroup (" +
                "br.amznAccount.id, " +
                "br.amznAccount.userName) " +
            "FROM BrgGroupAmznAccount as br " +
            "WHERE br.group.id = :id ")
    List<AmznAccAddedToGroup> getAmznAccInGroup(@Param("id") Integer id);

    @Query("SELECT NEW com.unimerch.dto.AmznAccAddedToGroup (" +
                "br.amznAccount.id, " +
                "br.amznAccount.userName) " +
            "FROM BrgGroupAmznAccount as br " +
            "WHERE br.amznAccount.id NOT IN ( " +
                "SELECT br.amznAccount.id " +
                "FROM BrgGroupAmznAccount as br " +
                "WHERE br.group.id = :id) ")
    List<AmznAccAddedToGroup> getAmznAccOutGroup(@Param("id") Integer id);

}
