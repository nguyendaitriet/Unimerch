package com.unimerch.mapper;

import com.unimerch.dto.analytics.ProductAnalyticsResult;
import com.unimerch.dto.product.ProductPriceParam;
import com.unimerch.dto.product.ProductResult;
import com.unimerch.repository.model.tag.BrgProductTag;
import com.unimerch.repository.model.product.Product;
import com.unimerch.repository.tag.BrgProductTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper{
    @Autowired
    private BrgProductTagRepository brgProductTagRepository;

    public Product toProduct(ProductPriceParam productPriceParam) {
        return new Product()
                .setId(productPriceParam.getAsin())
                .setPriceHtml(productPriceParam.getPriceHtml());
    }

    public ProductAnalyticsResult toProductAnalyticsResult(ProductResult productResult) {
        List<BrgProductTag> brgProductTagList = brgProductTagRepository.findByProductId(productResult.getAsin());
        return new ProductAnalyticsResult()
                .setProductResult(productResult)
                .setBrgProductTagList(brgProductTagList);
    }
}
