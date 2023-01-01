package com.unimerch.repository.tag;

import com.unimerch.repository.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("DELETE FROM BrgProductTag AS br " +
            "WHERE (br.tag.id = :tagId) ")
    @Modifying
    void deleteTagAssociateProduct(@Param("tagId") Integer tagId);

    @Query("DELETE FROM BrgTagTagContent AS br " +
            "WHERE (br.tag.id = :tagId) ")
    @Modifying
    void deleteTagAssociateTagContent(@Param("tagId") Integer tagId);


}
