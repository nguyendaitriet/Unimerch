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
@Table(name = "brg_tag_group_tag")
public class BrgTagGroupTag {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EmbeddedId
    private BrgTagGroupTagId id;

    @MapsId("tagGroupId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "tag_group_id", nullable = false)
    private TagGroup tagGroup;

    @MapsId("tagId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

}
