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
    private int quantitySold;
    private String productName;
    private BigDecimal royalties;
    private BigDecimal price;
    private int amznAccUsername;
    private String asin;
}
