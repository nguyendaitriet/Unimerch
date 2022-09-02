package com.unimerch.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer amznAccUsername;
    private String asin;
}
