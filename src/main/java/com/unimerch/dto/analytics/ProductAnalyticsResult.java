package com.unimerch.dto.analytics;

import com.unimerch.dto.product.ProductResult;
import com.unimerch.repository.model.tag.BrgProductTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProductAnalyticsResult {
    private ProductResult productResult;
    private List<BrgProductTag> brgProductTagList;
}
