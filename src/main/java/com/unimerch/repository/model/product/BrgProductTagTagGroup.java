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

}
