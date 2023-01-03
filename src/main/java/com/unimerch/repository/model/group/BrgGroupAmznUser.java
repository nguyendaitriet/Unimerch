package com.unimerch.repository.model.group;

import com.unimerch.repository.model.amzn_user.AmznUser;
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
@Table(name = "brg_group_amzn_user")
public class BrgGroupAmznUser {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EmbeddedId
    private BrgGroupAmznUserId id;

    @MapsId("groupId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @MapsId("amznUserId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "amzn_user_id")
    private AmznUser amznUser;

}