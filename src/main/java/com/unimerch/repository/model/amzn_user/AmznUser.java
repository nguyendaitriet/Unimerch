package com.unimerch.repository.model.amzn_user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(
        name = "amzn_users",
        indexes = @Index(name = "idx_last_check", columnList = "last_check")
)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AmznUser {
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
    @Enumerated(EnumType.STRING)
    private AzmnStatus status;

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

    public AmznUser(Integer id) {
        this.id = id;
    }

    public AmznUser(String username,String password) {
        this.password = password;
        this.username = username;
    }

    public AmznUser(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmznUser amznUser = (AmznUser) o;
        return Objects.equals(username, amznUser.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}