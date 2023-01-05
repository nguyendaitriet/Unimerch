package com.unimerch.mapper;

import com.unimerch.dto.analytics.ProductAnalyticsResult;
import com.unimerch.dto.product.ProductPriceParam;
import com.unimerch.dto.product.ProductResult;
import com.unimerch.dto.tag.TagGroupTagResult;
//import com.unimerch.repository.model.tag.BrgProductTagGroup;
import com.unimerch.repository.model.product.Product;
//import com.unimerch.repository.tag.BrgProductTagGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper{
//    @Autowired
//    private BrgProductTagGroupRepository brgProductTagGroupRepository;

    public Product toProduct(ProductPriceParam productPriceParam) {
        return new Product()
                .setId(productPriceParam.getAsin())
                .setPriceHtml(productPriceParam.getPriceHtml());
    }

    public ProductAnalyticsResult toProductAnalyticsResult(ProductResult productResult, List<TagGroupTagResult> tagGroupTagResultList) {
        return new ProductAnalyticsResult()
                .setProductResult(productResult)
                .setTagGroupTagResultList(tagGroupTagResultList);
    }
}
