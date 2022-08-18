package com.unimerch.repository;

import com.unimerch.dto.AmznAccAddedToGroup;
import com.unimerch.repository.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends
        JpaRepository<Group, Integer>
//        PagingAndSortingRepository<Group, Integer>
{

    boolean existsByTitle(String title);

    boolean existsById(int id);

    boolean existsByTitleAndIdIsNot(String title, int id);

//    Page<Group> findAllByTitleContains(String titleSearch, Pageable pageable);

}
