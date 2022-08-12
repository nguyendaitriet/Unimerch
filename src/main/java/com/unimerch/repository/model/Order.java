package com.unimerch.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")
@Accessors(chain = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ASIN", nullable = false, length = 150)
    private String asin;

    @Column(name = "date", nullable = false, updatable = false)
    private Instant date;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "info")
    private String info;

    @Column(name = "purchased", nullable = false)
    private Integer purchased;

    @Column(name = "cancelled", nullable = false)
    private Integer cancelled;

    @Column(name = "returned", nullable = false)
    private Integer returned;

    @Column(name = "revenue", nullable = false, precision = 12, scale = 2)
    private BigDecimal revenue;

    @Column(name = "royalties", nullable = false, precision = 12, scale = 2)
    private BigDecimal royalties;

    @Column(name = "currency", nullable = false, length = 50)
    private String currency;

    @ManyToOne(optional = false)
    @JoinColumn(name = "amzn_account_id", nullable = false)
    private AmznAccount amznAccount;


}