package com.unimerch.repository;

import com.unimerch.repository.model.BrgGroupUser;
import com.unimerch.repository.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrgGroupUserRepository extends JpaRepository<BrgGroupUser, Integer> {
    @Query("SELECT NEW com.unimerch.repository.model.Group " +
                "(br.group.id, br.group.title)" +
            "FROM BrgGroupUser AS br " +
            "WHERE br.user.id=:id")
    List<Group> findAllGroupByUserId(@Param("id") Integer userId);
}
