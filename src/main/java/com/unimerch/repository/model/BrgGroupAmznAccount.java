package com.unimerch.repository.model;

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
public class BrgGroupAmznAccount {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EmbeddedId
    private BrgGroupAmznAccountId id;

    @MapsId("groupId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @MapsId("amznAccountId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "amzn_account_id")
    private AmznUser amznAccount;


}