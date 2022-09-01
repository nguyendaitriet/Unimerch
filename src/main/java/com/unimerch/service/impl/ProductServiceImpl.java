package com.unimerch.service.impl;

import com.unimerch.dto.product.ProductItemResult;
import com.unimerch.repository.ProductRepository;
import com.unimerch.repository.datatable.ProductTableRepository;
import com.unimerch.repository.model.Order;
import com.unimerch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductItemResult> findAllTodaySoldProduct(Integer amznAccId) {
        Instant randomDate = Instant.parse("2022-07-02T00:00:00.000-07:00");

//        List<ProductItemResult> productItemResultList =
//                productRepository.getProductItemResultList(randomDate, amznAccId)
//                        .stream()
//                        .map(t -> new ProductItemResult(
//                                t.get(0, Integer.class),
//                                t.get(1, String.class),
//                                t.get(2, BigDecimal.class),
//                                t.get(3, BigDecimal.class),
//                                t.get(4, Integer.class),
//                                t.get(5, String.class)
//                        ))
//                        .collect(Collectors.toList());
        return null;
    }

}
