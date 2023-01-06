package com.unimerch.repository.model.product;

import com.unimerch.repository.model.tag.BrgTagGroupTag;
import com.unimerch.repository.model.tag.Tag;
import com.unimerch.repository.model.tag.TagGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        name = "brg_product_tag_tag_group",
        indexes = @Index(name = "idx_asin", columnList = "ASIN")
)
public class BrgProductTagTagGroup {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EmbeddedId
    private BrgProductTagTagGroupId id;

//    @MapsId("productId")
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "ASIN", nullable = false)
//    private Product product;

//    @MapsId("tagGroupId")
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "tag_group_id", nullable = false)
//    private TagGroup tagGroup;
//
//    @MapsId("tagId")
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "tag_id", nullable = false)
//    private Tag tag;

//    @ManyToOne
//    @JoinColumns({
//            @JoinColumn(referencedColumnName="tag_group_id"),
//            @JoinColumn(referencedColumnName="tag_id")
//    })
//    private BrgTagGroupTag tagGroupTag;
}
