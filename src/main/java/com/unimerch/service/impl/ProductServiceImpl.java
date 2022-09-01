package com.unimerch.service.impl;

import com.unimerch.dto.product.ProductItemResult;
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
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<ProductItemResult> findAllTodaySoldProduct(Integer amznAccId) {
        return null;
    }


}
