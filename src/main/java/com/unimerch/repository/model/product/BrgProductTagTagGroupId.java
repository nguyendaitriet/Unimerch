package com.unimerch.repository.model.product;

import com.unimerch.repository.model.tag.BrgTagGroupTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class BrgProductTagTagGroupId implements Serializable {

    private static final long serialVersionUID = 1478932344399547886L;
    @Column(name = "ASIN", nullable = false, length = 150)
    private String productId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "tag_group_id", referencedColumnName = "tag_group_id"),
            @JoinColumn(name = "tag_id", referencedColumnName = "tag_id")
    })
    private BrgTagGroupTag brgTagGroupTag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BrgProductTagTagGroupId entity = (BrgProductTagTagGroupId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.brgTagGroupTag, entity.brgTagGroupTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, brgTagGroupTag);
    }


}