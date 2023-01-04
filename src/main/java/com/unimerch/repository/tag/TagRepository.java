package com.unimerch.repository.tag;

import com.unimerch.repository.model.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT t " +
            "FROM Tag AS t " +
            "INNER JOIN BrgTagGroupTag AS b " +
            "ON t.id = b.tag.id " +
            "WHERE b.tagGroup.id = :tagGroupId ")
    List<Tag> findAllTagInsideTagGroup(@Param("tagGroupId") Integer tagGroupId);

    @Query("SELECT t " +
            "FROM Tag AS t " +
            "WHERE t.id NOT IN ( " +
                "SELECT b.tag.id " +
                "FROM BrgTagGroupTag AS b " +
                "WHERE b.tagGroup.id = :tagGroupId " +
            ")"
    )
    List<Tag> findAllTagOutsideTagGroup(@Param("tagGroupId") Integer tagGroupId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BrgTagGroupTag AS b " +
            "WHERE b.tag.id = :tagId AND b.tagGroup.id = :tagGroupId")
    void deleteTagFromTagGroup(@Param("tagId") Integer tagId, @Param("tagGroupId") Integer tagGroupId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BrgTagGroupTag AS b " +
            "WHERE b.tag.id IN :tagIdList AND b.tagGroup.id = :tagGroupId")
    void deleteMultiTagFromTagGroup(@Param("tagIdList") List<Integer> tagIdList, @Param("tagGroupId") Integer tagGroupId);

    @Modifying
    void deleteAllByIdIn(List<Integer> tagIdList);
}
