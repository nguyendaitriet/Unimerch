package com.unimerch.repository.tag;

import com.unimerch.repository.model.tag.TagGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface TagGroupRepository extends JpaRepository<TagGroup, Integer> {
    @Query("DELETE FROM BrgProductTagGroup AS br " +
            "WHERE (br.tagGroup.id = :tagGroupId) ")
    @Modifying
    void deleteTagGroupAssociateProduct(@Param("tagGroupId") Integer tagGroupId);

    @Query("DELETE FROM BrgTagGroupTag AS br " +
            "WHERE (br.tagGroup.id = :tagGroupId) ")
    @Modifying
    void deleteTagGroupAssociateTag(@Param("tagGroupId") Integer tagGroupId);


}
