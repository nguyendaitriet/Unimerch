package com.unimerch.dto.analytics;

import com.unimerch.dto.product.ProductResult;
import com.unimerch.repository.model.BrgProductTag;
import com.unimerch.repository.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
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
