package com.unimerch.repository;

import com.unimerch.repository.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdIsNot(String title, int id);

    @Query("DELETE FROM BrgGroupAmznAccount AS br " +
            "WHERE (br.group.id = :groupId) ")
    @Modifying
    void deleteGroupAssociateAmznAcc(@Param("groupId") Integer groupId);

    @Query("DELETE FROM BrgGroupUser AS br " +
            "WHERE (br.group.id = :groupId) ")
    @Modifying
    void deleteGroupAssociateUser(@Param("groupId") Integer groupId);

    @Query("DELETE FROM Group AS g " +
            "WHERE (g.id = :groupId) ")
    @Modifying
    void deleteGroup(@Param("groupId") Integer groupId);

}
