package com.unimerch.repository.model.amzn_user;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "AmazoneAccounts")
@Accessors(chain = true)
public class AmazoneAccounts {
    @Id
    @Column(name = "User_ID")
    private String User_ID;
    @Column(name = "Password")
    private String Password;
    @Column(name = "EmailAddress")
    private String EmailAddress;
    @Column(name = "Del")
    private String Del;
    @Column(name = "rowguid")
    private String rowguid;
    @Column(name = "access_token")
    private String access_token;
    @Column(name = "LastCheck")
    private String LastCheck;
    @Column(name = "AccountStatus")
    private String AccountStatus;
}