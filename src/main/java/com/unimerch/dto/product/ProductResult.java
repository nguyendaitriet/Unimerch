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
public class ProductResult {
    private Long quantitySold;
    private String productName;
    private BigDecimal royalties;
    private BigDecimal price;
    private String amznAccUsername;
    private String asin;
}
