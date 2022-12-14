//package com.unimerch.repository.model.tag;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//import org.hibernate.Hibernate;
//
//import javax.persistence.Column;
//import javax.persistence.Embeddable;
//import java.io.Serializable;
//import java.util.Objects;
//
//@Embeddable
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Accessors(chain = true)
//public class BrgProductTagGroupId implements Serializable {
//
//    private static final long serialVersionUID = 1478932386399547886L;
//    @Column(name = "ASIN", nullable = false, length = 150)
//    private String productId;
//
//    @Column(name = "tag_id", nullable = false)
//    private Integer tagGroupId;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        BrgProductTagGroupId entity = (BrgProductTagGroupId) o;
//        return Objects.equals(this.productId, entity.productId) &&
//                Objects.equals(this.tagGroupId, entity.tagGroupId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(productId, tagGroupId);
//    }
//
//}