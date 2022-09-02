package com.unimerch.service.impl;

import com.unimerch.dto.product.ProductItemResult;
import com.unimerch.repository.ProductRepository;
import com.unimerch.repository.datatable.ProductTableRepository;
import com.unimerch.repository.model.Order;
import com.unimerch.service.ProductService;
import com.unimerch.util.TimeUtils;
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

    @Autowired
    private TimeUtils timeUtils;

    @Override
    public List<ProductItemResult> findAllTodaySoldProduct(Integer amznAccId) {
        Instant today = timeUtils.getInstantToday();
        return productRepository.getProductItemResultList(today, amznAccId);
    }

    @Override
    public List<ProductItemResult> findAllThisMonthSoldProduct(Integer amznAccId) {
        Instant firstDayOfThisMonth = timeUtils.getInstantThisMonth();
        return productRepository.getProductItemResultList(firstDayOfThisMonth, amznAccId);
    }

    @Override
    public List<ProductItemResult> findAllLast30DaysSoldProduct(Integer amznAccId) {
        Instant startDate = timeUtils.getInstantLastSomeDays(30);
        return productRepository.getProductItemResultList(startDate, amznAccId);
    }

}
