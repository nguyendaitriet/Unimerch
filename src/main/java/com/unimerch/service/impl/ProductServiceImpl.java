package com.unimerch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.unimerch.dto.product.ProductResult;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.ProductMapper;
import com.unimerch.repository.BrgGroupAmznUserRepository;
import com.unimerch.repository.ProductRepository;
import com.unimerch.repository.model.Product;
import com.unimerch.service.ProductService;
import com.unimerch.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BrgGroupAmznUserRepository brgGroupAmznUserRepository;

    @Override
    public List<ProductResult> findAllTodaySoldProduct(Integer id, int choice) {
        Instant today = TimeUtils.getInstantToday();
        return getProductItemResult(today, id, choice);
    }

    @Override
    public List<ProductResult> findAllThisMonthSoldProduct(Integer id, int choice) {
        Instant firstDayOfThisMonth = TimeUtils.getInstantThisMonth();
        return getProductItemResult(firstDayOfThisMonth, id, choice);
    }

    @Override
    public List<ProductResult> findAllLast30DaysSoldProduct(Integer id, int choice) {
        Instant startDate = TimeUtils.getInstantLastSomeDays(30);
        return getProductItemResult(startDate, id, choice);
    }

    @Override
    public void updateProduct(String data) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Product.class, new ProductMapper());
        mapper.registerModule(module);
        try {
            Product productData = mapper.readValue(data, Product.class);
            productRepository.save(productData);
        } catch (JsonProcessingException | ServerErrorException e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    private List<ProductResult> getProductItemResult(Instant instant, Integer id, int choice) {
        List<ProductResult> productResultList = new ArrayList<>();
        List<Integer> amznAccIdList = new ArrayList<>();
        switch (choice) {
            //With amazon account id
            case 1:
                amznAccIdList.add(id);
                productResultList = productRepository.getProductItemResultList(instant, amznAccIdList);
                break;
            //With group id
            case 2:
                amznAccIdList.addAll(brgGroupAmznUserRepository.getAmznAccIdInGroup(id));
                productResultList = productRepository.getProductItemResultList(instant, amznAccIdList);
                break;
            //All amazon account
            case 3:
                productResultList = productRepository.getProductItemResultList(instant);
                break;
        }
        return productResultList;
    }

}
