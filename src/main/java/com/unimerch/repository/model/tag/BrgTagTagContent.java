package com.unimerch.repository.model.tag;

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
@Table(name = "brg_tag_tagContent")
public class BrgTagTagContent {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EmbeddedId
    private BrgTagTagContentId id;

    @MapsId("tagId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @MapsId("tagContentId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "tagContent_id", nullable = false)
    private TagContent tagContent;

}
