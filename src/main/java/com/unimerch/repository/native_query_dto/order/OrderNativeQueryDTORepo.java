package com.unimerch.repository.native_query_dto.order;

import com.unimerch.dto.order.OrderChartColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderNativeQueryDTORepo extends JpaRepository<OrderNativeQueryDTO, Integer> {
    @Query(name = "get_order_chart_column_result_all_acc", nativeQuery = true)
    List<OrderChartColumn> findAllOrderChartWithDateRange(@Param("startDay") Instant startDay, @Param("endDay") Instant endDay);

    @Query(name = "get_order_chart_column_result_group", nativeQuery = true)
    List<OrderChartColumn> findGroupOrderChartWithDateRange(@Param("groupId") Integer groupId, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay);

    @Query(name = "get_order_chart_column_result_amzn", nativeQuery = true)
    List<OrderChartColumn> findAmznOrderChartWithDateRange(@Param("amznId") Integer amznId, @Param("startDay") Instant startDay, @Param("endDay") Instant endDay);

}
