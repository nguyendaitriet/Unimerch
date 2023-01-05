package com.unimerch.repository.product;

import com.unimerch.dto.tag.TagGroupTagResult;
import com.unimerch.repository.model.product.BrgProductTagTagGroup;
//import com.unimerch.repository.model.tag.BrgProductTagGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrgProductTagTagGroupRepository extends JpaRepository<BrgProductTagTagGroup, Integer> {

    @Query("SELECT NEW com.unimerch.dto.tag.TagGroupTagResult(" +
                "b.id.brgTagGroupTag.tagGroup.id," +
                "b.id.brgTagGroupTag.tagGroup.color," +
                "b.id.brgTagGroupTag.tag.name" +
            ") " +
            "FROM BrgProductTagTagGroup AS b " +
            "WHERE b.product.id = :asin"
    )
    List<TagGroupTagResult> findTagGroupAndTagByAsin(@Param("asin") String asin);
}
