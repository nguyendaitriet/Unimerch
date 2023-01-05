//package com.unimerch.repository.model.tag;
//
//import com.unimerch.repository.model.product.Product;
//import lombok.*;
//
//import javax.persistence.*;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Table(
//        name = "brg_product_tagGroup",
//        indexes = @Index(name = "idx_asin", columnList = "ASIN")
//)
//public class BrgProductTagGroup {
//
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @EmbeddedId
//    private BrgProductTagGroupId id;
//
//    @MapsId("productId")
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "ASIN", nullable = false)
//    private Product product;
//
//    @MapsId("tagGroupId")
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "tag_group_id", nullable = false)
//    private TagGroup tagGroup;
//
//}
