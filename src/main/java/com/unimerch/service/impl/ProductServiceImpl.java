package com.unimerch.service.impl;

import com.unimerch.dto.product.ProductPriceParam;
import com.unimerch.dto.product.ProductResult;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.ProductMapper;
import com.unimerch.repository.group.BrgGroupAmznUserRepository;
import com.unimerch.repository.product.ProductRepository;
import com.unimerch.repository.model.Product;
import com.unimerch.service.ProductService;
import com.unimerch.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BrgGroupAmznUserRepository brgGroupAmznUserRepository;

    @Autowired
    private ConfigurationServiceImpl configurationService;

    @Autowired
    private ProductMapper productMapper;

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
    public void updateProduct(ProductPriceParam productData) {
        try {
            BigDecimal price = convertToProductPrice(productData.getPriceHtml());
            Product product = productMapper.toProduct(productData);
            product.setPrice(price);
            productRepository.save(product);
        } catch (ServerErrorException e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    private BigDecimal convertToProductPrice(String priceHtml) {
        String priceRegex = configurationService.getBackendPricePattern();
        Pattern TAG_REGEX = Pattern.compile(priceRegex);
        Matcher matcher = TAG_REGEX.matcher(priceHtml);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(0));
        }
        return null;
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
