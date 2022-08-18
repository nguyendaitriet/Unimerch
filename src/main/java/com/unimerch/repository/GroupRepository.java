package com.unimerch.repository;

import com.unimerch.repository.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer>
{

    boolean existsByTitle(String title);

    boolean existsById(int id);

    boolean existsByTitleAndIdIsNot(String title, int id);

}
