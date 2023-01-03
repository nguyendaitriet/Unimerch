package com.unimerch.dto.product;

import com.unimerch.repository.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {
    private List<Product> productList;
}
