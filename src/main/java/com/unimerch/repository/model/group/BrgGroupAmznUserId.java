package com.unimerch.repository.model.group;

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
public class BrgGroupAmznUserId implements Serializable {

    private static final long serialVersionUID = 1461032386399547886L;
    @Column(name = "group_id", nullable = false)
    private Integer groupId;

    @Column(name = "amzn_user_id", nullable = false)
    private Integer amznUserId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BrgGroupAmznUserId entity = (BrgGroupAmznUserId) o;
        return Objects.equals(this.groupId, entity.groupId) &&
                Objects.equals(this.amznUserId, entity.amznUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, amznUserId);
    }

}