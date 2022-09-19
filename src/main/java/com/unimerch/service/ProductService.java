package com.unimerch.service;

import com.unimerch.dto.product.ProductResult;

import java.util.List;

public interface ProductService {

    List<ProductResult> findAllTodaySoldProduct(Integer id, int choice);

    List<ProductResult> findAllThisMonthSoldProduct(Integer id, int choice);

    List<ProductResult> findAllLast30DaysSoldProduct(Integer id, int choice);

    void updateProduct(String productData);
}
