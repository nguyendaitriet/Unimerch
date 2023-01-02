package com.unimerch.repository.group;

import com.unimerch.repository.model.group.BrgGroupUser;
import com.unimerch.repository.model.group.Group;
import com.unimerch.repository.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrgGroupUserRepository extends JpaRepository<BrgGroupUser, Integer> {
    @Query("SELECT NEW com.unimerch.repository.model.group.Group " +
            "(br.group.id, br.group.title)" +
            "FROM BrgGroupUser AS br " +
            "WHERE br.user.id=:id")
    List<Group> findAssignedGroupsByUserId(@Param("id") Integer userId);

    @Query("SELECT NEW com.unimerch.repository.model.group.Group " +
            "(g.id, g.title)" +
            "FROM Group AS g " +
            "WHERE g.id NOT IN " +
                "(SELECT br.group.id FROM " +
                "BrgGroupUser AS br " +
                "WHERE br.user.id=:id)")
    List<Group> findUnassignedGroupsByUserId(@Param("id") Integer userId);

    @Modifying
    @Query("DELETE FROM BrgGroupUser AS br " +
            "WHERE br.group=:group " +
            "AND br.user=:user")
    void removeGroupFromUser(@Param("group") Group group, @Param("user") User user);
}
