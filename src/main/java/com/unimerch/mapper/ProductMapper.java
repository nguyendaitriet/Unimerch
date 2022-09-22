package com.unimerch.mapper;

import com.unimerch.dto.product.ProductPriceParam;
import com.unimerch.repository.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper{

    public Product toProduct(ProductPriceParam productPriceParam) {
        return new Product()
                .setId(productPriceParam.getAsin())
                .setPriceHtml(productPriceParam.getPriceHtml());
    }
}
