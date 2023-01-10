package com.unimerch.service.product;

import com.unimerch.dto.product.ProductPriceParam;
import com.unimerch.dto.product.ProductResult;

import java.util.List;

public interface ProductService {

    List<ProductResult> findAllTodaySoldProduct(Integer id, int choice);

    List<ProductResult> findAllThisMonthSoldProduct(Integer id, int choice);

    List<ProductResult> findAllLast30DaysSoldProduct(Integer id, int choice);

    void updateProduct(ProductPriceParam productData);

    void updateProducts(List<ProductPriceParam> productData);
}
