package com.unimerch.repository.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "products")
@Accessors(chain = true)
public class Product {
    @Id
    @Column(name = "ASIN", nullable = false, length = 150)
    private String id;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @Lob
    @Column(name = "price_HTML")
    private String priceHtml;

    public Product(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}