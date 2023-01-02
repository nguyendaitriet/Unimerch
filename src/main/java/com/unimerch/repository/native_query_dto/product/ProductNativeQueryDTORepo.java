package com.unimerch.repository.native_query_dto.product;

import com.unimerch.dto.analytics.ProductAnalyticsResult;
import com.unimerch.dto.product.ProductResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ProductNativeQueryDTORepo extends JpaRepository<ProductNativeQueryDTO, Integer> {

    @Query(name = "get_product_item_result", nativeQuery = true)
    List<ProductResult> getProductItemResultList(@Param("startDay") Instant startDay, @Param("amznAccIds") List<Integer> amznAccIds);

}
