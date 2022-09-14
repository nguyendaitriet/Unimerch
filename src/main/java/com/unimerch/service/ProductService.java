package com.unimerch.service;

import com.unimerch.dto.product.ProductItemResult;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

public interface ProductService {

    List<ProductItemResult> findAllTodaySoldProduct(Integer id, int choice);

    List<ProductItemResult> findAllThisMonthSoldProduct(Integer id, int choice);

    List<ProductItemResult> findAllLast30DaysSoldProduct(Integer id, int choice);

    void updateProduct(String productData);
}
