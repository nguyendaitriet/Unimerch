package com.unimerch.repository.model.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class BrgTagTagContentId implements Serializable {

    private static final long serialVersionUID = 1461122386399547886L;
    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

    @Column(name = "tag_content_id", nullable = false)
    private Integer tagContentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BrgTagTagContentId entity = (BrgTagTagContentId) o;
        return Objects.equals(this.tagId, entity.tagId) &&
                Objects.equals(this.tagContentId, entity.tagContentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, tagContentId);
    }

}