package com.unimerch.service.impl;

import com.unimerch.dto.product.ProductItemResult;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.repository.BrgGroupAmznAccountRepository;
import com.unimerch.repository.ProductRepository;
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
    private TimeUtils timeUtils;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BrgGroupAmznAccountRepository brgGroupAmznAccountRepository;

    @Override
    public List<ProductItemResult> findAllTodaySoldProduct(Integer id, int choice) {
        Instant today = timeUtils.getInstantToday();
        return getProductItemResult(today, id, choice);
    }

    @Override
    public List<ProductItemResult> findAllThisMonthSoldProduct(Integer id, int choice) {
        Instant firstDayOfThisMonth = timeUtils.getInstantThisMonth();
        return getProductItemResult(firstDayOfThisMonth, id, choice);
    }

    @Override
    public List<ProductItemResult> findAllLast30DaysSoldProduct(Integer id, int choice) {
        Instant startDate = timeUtils.getInstantLastSomeDays(30);
        return getProductItemResult(startDate, id, choice);
    }

    private List<ProductItemResult> getProductItemResult(Instant instant, Integer id, int choice) {
        List<ProductItemResult> productItemResultList = new ArrayList<>();
        List<Integer> amznAccIdList = new ArrayList<>();
        switch (choice) {
            //With amazon account id
            case 1:
                amznAccIdList.add(id);
                productItemResultList = productRepository.getProductItemResultList(instant, amznAccIdList);
                break;
            //With group id
            case 2:
                amznAccIdList.addAll(brgGroupAmznAccountRepository.getAmznAccIdInGroup(id));
                productItemResultList = productRepository.getProductItemResultList(instant, amznAccIdList);
                break;
            //All amazon account
            case 3:
                productItemResultList = productRepository.getProductItemResultList(instant);
                break;
        }
        if (productItemResultList.isEmpty()) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }
        return productItemResultList;
    }

}
