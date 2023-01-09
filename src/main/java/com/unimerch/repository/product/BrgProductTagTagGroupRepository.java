package com.unimerch.repository.product;

import com.unimerch.dto.tag.TagGroupTagIdResult;
import com.unimerch.dto.tag.TagGroupTagResult;
import com.unimerch.repository.model.product.BrgProductTagTagGroup;
//import com.unimerch.repository.model.tag.BrgProductTagGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BrgProductTagTagGroupRepository extends JpaRepository<BrgProductTagTagGroup, Integer> {

    @Query("SELECT NEW com.unimerch.dto.tag.TagGroupTagResult(" +
                "b.id.brgTagGroupTag.tagGroup.id," +
                "b.id.brgTagGroupTag.tagGroup.color," +
                "b.id.brgTagGroupTag.tag.name" +
            ") " +
            "FROM BrgProductTagTagGroup AS b " +
            "WHERE b.id.productId = :asin"
    )
    List<TagGroupTagResult> findTagGroupAndTagByAsin(@Param("asin") String asin);

    @Query("SELECT NEW com.unimerch.dto.tag.TagGroupTagIdResult(" +
                "CONCAT('productTag-',b.id.brgTagGroupTag.tagGroup.id,'-',b.id.brgTagGroupTag.tag.id) " +
            ") " +
            "FROM BrgProductTagTagGroup AS b " +
            "WHERE b.id.productId = :asin"
    )
    List<TagGroupTagIdResult> findTagGroupAndTagIdByAsin(@Param("asin") String asin);

    @Query(value = "DELETE FROM brg_product_tag_tag_group AS b " +
            "WHERE b.asin = :asin", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteAllByAsin(@Param("asin") String asin);
}
