package com.unimerch.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductItemResult {
    private Integer quantitySold;
    private String productName;
    private BigDecimal royalties;
    private BigDecimal price;
    private String amznAccUsername;
    private String asin;

    public ProductItemResult(Long quantitySold, String productName, BigDecimal royalties, BigDecimal price, String amznAccUsername, String asin) {
        this.quantitySold = quantitySold.intValue();
        this.productName = productName;
        this.royalties = royalties;
        this.price = price;
        this.amznAccUsername = amznAccUsername;
        this.asin = asin;
    }
}
