package com.unimerch.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "ASIN", nullable = false, length = 150)
    private String id;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @Lob
    @Column(name = "price_HTML")
    private String priceHtml;


}