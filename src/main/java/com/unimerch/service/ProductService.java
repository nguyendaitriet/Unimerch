package com.unimerch.service;

import com.unimerch.dto.product.ProductItemResult;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

public interface ProductService {
    List<ProductItemResult> findAllTodaySoldProduct(Integer amznAccId);
}
