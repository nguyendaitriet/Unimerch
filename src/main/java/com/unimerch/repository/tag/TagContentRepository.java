package com.unimerch.repository.tag;

import com.unimerch.repository.model.tag.TagContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TagContentRepository extends JpaRepository<TagContent, Integer> {

    @Query("SELECT t " +
            "FROM TagContent AS t " +
            "WHERE t.id IN (:idList) ")
    List<TagContent> findAllTagContentByIds(@Param("idList") List<Integer> idList);

    @Query("SELECT t " +
            "FROM TagContent AS t " +
            "INNER JOIN BrgTagTagContent AS b " +
            "ON t.id = b.tagContent.id " +
            "WHERE b.tag.id = :tagId ")
    List<TagContent> findAllTagContentInsideTag(@Param("tagId") Integer tagId);

    @Query("SELECT t " +
            "FROM TagContent AS t " +
            "WHERE t.id NOT IN ( " +
                "SELECT b.tagContent.id " +
                "FROM BrgTagTagContent AS b " +
                "WHERE b.tag.id = :tagId " +
            ")"
    )
    List<TagContent> findAllTagContentOutsideTag(@Param("tagId") Integer tagId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BrgTagTagContent AS b " +
            "WHERE b.tagContent.id = :tagContentId AND b.tag.id = :tagId")
    void deleteTagContentFromTag(@Param("tagContentId") Integer tagContentId, @Param("tagId") Integer tagId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BrgTagTagContent AS b " +
            "WHERE b.tagContent.id IN :tagContentIdList AND b.tag.id = :tagId")
    void deleteMultiTagContentFromTag(@Param("tagContentIdList") List<Integer> tagContentIdList, @Param("tagId") Integer tagId);

}
