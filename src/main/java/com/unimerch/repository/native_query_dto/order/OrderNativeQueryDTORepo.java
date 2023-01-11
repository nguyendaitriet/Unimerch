package com.unimerch.repository.native_query_dto.order;

import com.unimerch.dto.order.OrderCardResult;
import com.unimerch.dto.order.OrderChartColumn;
import com.unimerch.dto.product.ProductResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderNativeQueryDTORepo extends JpaRepository<OrderNativeQueryDTO, Integer> {
    @Query(name = "get_order_chart_column_result_all_acc", nativeQuery = true)
    List<OrderChartColumn> findAllOrderChartWithDateRange(@Param("startDay") Instant startDay, @Param("endDay") Instant endDay,
                                                          @Param("tagIncluded") List<String> tagIncluded, @Param("includedSize") Integer includedSize,
                                                          @Param("tagExcluded") List<String> tagExcluded, @Param("excludedSize") Integer excludedSize);

    @Query(name = "get_order_chart_column_result_group", nativeQuery = true)
    List<OrderChartColumn> findGroupOrderChartWithDateRange(@Param("groupId") Integer groupId, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay,
                                                            @Param("tagIncluded") List<String> tagIncluded, @Param("includedSize") Integer includedSize,
                                                            @Param("tagExcluded") List<String> tagExcluded, @Param("excludedSize") Integer excludedSize);

    @Query(name = "get_order_chart_column_result_amzn", nativeQuery = true)
    List<OrderChartColumn> findAmznOrderChartWithDateRange(@Param("amznId") Integer amznId, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay,
                                                           @Param("tagIncluded") List<String> tagIncluded, @Param("includedSize") Integer includedSize,
                                                           @Param("tagExcluded") List<String> tagExcluded, @Param("excludedSize") Integer excludedSize);

    @Query(name = "get_all_product_analytics", nativeQuery = true)
    List<ProductResult> getAllProductAnalyticsList(@Param("startDay") Instant startDay, @Param("endDay") Instant endDay,
                                                   @Param("tagIncluded") List<String> tagIncluded, @Param("includedSize") Integer includedSize,
                                                   @Param("tagExcluded") List<String> tagExcluded, @Param("excludedSize") Integer excludedSize,
                                                   @Param("searchable") boolean searchable, @Param("key") String key);

    @Query(name = "get_group_product_analytics", nativeQuery = true)
    List<ProductResult> getGroupProductAnalyticsList(@Param("groupId") Integer groupId, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay,
                                                   @Param("tagIncluded") List<String> tagIncluded, @Param("includedSize") Integer includedSize,
                                                     @Param("tagExcluded") List<String> tagExcluded, @Param("excludedSize") Integer excludedSize,
                                                   @Param("searchable") boolean searchable, @Param("key") String key);

    @Query(name = "get_amzn_product_analytics", nativeQuery = true)
    List<ProductResult> getAmznProductAnalyticsList(@Param("amznId") Integer amznId, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay,
                                                   @Param("tagIncluded") List<String> tagIncluded, @Param("includedSize") Integer includedSize,
                                                    @Param("tagExcluded") List<String> tagExcluded, @Param("excludedSize") Integer excludedSize,
                                                   @Param("searchable") boolean searchable, @Param("key") String key);

    @Query(name = "get_all_order_cart_result", nativeQuery = true)
    OrderCardResult getAllOrderCartResult(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate,
                                          @Param("tagIncluded") List<String> tagIncluded, @Param("includedSize") Integer includedSize,
                                          @Param("tagExcluded") List<String> tagExcluded, @Param("excludedSize") Integer excludedSize);

    @Query(name = "get_group_order_cart_result", nativeQuery = true)
    OrderCardResult getGroupOrderCartResult(@Param("groupId") Integer groupId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate,
                                          @Param("tagIncluded") List<String> tagIncluded, @Param("includedSize") Integer includedSize,
                                            @Param("tagExcluded") List<String> tagExcluded, @Param("excludedSize") Integer excludedSize);

    @Query(name = "get_amzn_order_cart_result", nativeQuery = true)
    OrderCardResult getAmznOrderCartResult(@Param("amznId") Integer amznId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate,
                                          @Param("tagIncluded") List<String> tagIncluded, @Param("includedSize") Integer includedSize,
                                           @Param("tagExcluded") List<String> tagExcluded, @Param("excludedSize") Integer excludedSize);

}
