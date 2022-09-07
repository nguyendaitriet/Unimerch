package com.unimerch.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "amzn_accounts")
@Accessors(chain = true)
public class AmznAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "last_check")
    private Instant lastCheck;

    @Column(name = "account_status", length = 50)
    private String accountStatus;

    @Column(name = "daily_product_count", nullable = false)
    private Integer dailyProductCount;

    @Column(name = "daily_product_limit", nullable = false)
    private Integer dailyProductLimit;

    @Column(name = "overall_product_count", nullable = false)
    private Integer overallProductCount;

    @Column(name = "overall_product_limit", nullable = false)
    private Integer overallProductLimit;

    @Column(name = "overall_design_count", nullable = false)
    private Integer overallDesignCount;

    @Column(name = "overall_design_limit", nullable = false)
    private Integer overallDesignLimit;

    @Column(name = "tier", nullable = false)
    private Integer tier;

    @Column(name = "total_rejected", nullable = false)
    private Integer totalRejected;

    @Column(name = "total_removed", nullable = false)
    private Integer totalRemoved;

    @Column(name = "note", length = 50)
    private String note;

}