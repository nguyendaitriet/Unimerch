package com.unimerch.repository.native_query_dto.order;

import com.unimerch.dto.order.OrderChartColumn;
import com.unimerch.dto.product.ProductResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "order_native_query_dto")

@NamedNativeQuery(
        name = "get_order_chart_column_result_all_acc",
        query =
                "SELECT  " +
                    "STR_TO_DATE(DATE(o.`date`),'%Y-%m-%d') AS date, " +
                    "SUM(o.royalties) AS royalties, " +
                    "SUM(o.purchased - o.cancelled) AS sold " +
                "FROM orders o " +
                "WHERE o.date BETWEEN :startDay AND :endDay " +
                "GROUP BY STR_TO_DATE(DATE(o.`date`),'%Y-%m-%d') " +
                "ORDER BY STR_TO_DATE(DATE(o.`date`),'%Y-%m-%d') ",
        resultSetMapping = "order_chart_column_result_all_acc"
)
@SqlResultSetMapping(
        name = "order_chart_column_result_all_acc",
        classes = @ConstructorResult(
                targetClass = OrderChartColumn.class,
                columns = {
                        @ColumnResult(name = "date", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "sold", type = Integer.class),
                }
        )
)

@NamedNativeQuery(
        name = "get_order_chart_column_result_group",
        query =
                "SELECT  " +
                    "STR_TO_DATE(DATE(o.`date`),'%Y-%m-%d') AS date, " +
                    "SUM(o.royalties) AS royalties, " +
                    "SUM(o.purchased - o.cancelled) AS sold " +
                "FROM orders o " +
                "WHERE o.date BETWEEN :startDay AND :endDay " +
                "AND o.amzn_user_id IN ( " +
                "SELECT b.amzn_user_id " +
                "FROM brg_group_amzn_user AS b " +
                "WHERE b.group_id = :groupId " +
                ") " +
                "GROUP BY STR_TO_DATE(DATE(o.`date`),'%Y-%m-%d') " +
                "ORDER BY STR_TO_DATE(DATE(o.`date`),'%Y-%m-%d') ",
        resultSetMapping = "order_chart_column_result_group"
)
@SqlResultSetMapping(
        name = "order_chart_column_result_group",
        classes = @ConstructorResult(
                targetClass = OrderChartColumn.class,
                columns = {
                        @ColumnResult(name = "date", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "sold", type = Integer.class),
                }
        )
)

@NamedNativeQuery(
        name = "get_order_chart_column_result_amzn",
        query =
                "SELECT  " +
                        "STR_TO_DATE(DATE(o.`date`),'%Y-%m-%d') AS date, " +
                        "SUM(o.royalties) AS royalties, " +
                        "SUM(o.purchased - o.cancelled) AS sold " +
                        "FROM orders o " +
                        "WHERE o.date BETWEEN :startDay AND :endDay " +
                        "AND o.amzn_user_id = :amznId " +
                        "GROUP BY STR_TO_DATE(DATE(o.`date`),'%Y-%m-%d') " +
                        "ORDER BY STR_TO_DATE(DATE(o.`date`),'%Y-%m-%d')",
        resultSetMapping = "order_chart_column_result_amzn"
)
@SqlResultSetMapping(
        name = "order_chart_column_result_amzn",
        classes = @ConstructorResult(
                targetClass = OrderChartColumn.class,
                columns = {
                        @ColumnResult(name = "date", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "sold", type = Integer.class),
                }
        )
)

public class OrderNativeQueryDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
}
