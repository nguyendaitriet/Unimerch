package com.unimerch.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BrgGroupAmznAccountId implements Serializable {
    private static final long serialVersionUID = 1461032386399547886L;
    @Column(name = "group_id", nullable = false)
    private Integer groupId;

    @Column(name = "amzn_account_id", nullable = false)
    private Integer amznAccountId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BrgGroupAmznAccountId entity = (BrgGroupAmznAccountId) o;
        return Objects.equals(this.groupId, entity.groupId) &&
                Objects.equals(this.amznAccountId, entity.amznAccountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, amznAccountId);
    }

}