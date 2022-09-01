package com.unimerch.service;

import com.unimerch.dto.product.ProductItemResult;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface OrderService {
    void saveOrderData(String data, String jwt);
}
