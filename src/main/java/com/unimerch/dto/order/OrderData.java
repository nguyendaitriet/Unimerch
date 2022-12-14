package com.unimerch.dto.order;

import com.unimerch.repository.model.order.Order;
import com.unimerch.repository.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderData {
    private List<Order> orderList;
    private Set<String> asinList;
    private List<Product> productList;
}