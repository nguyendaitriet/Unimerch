package com.unimerch.repository.native_query_dto.order;

import com.unimerch.dto.order.OrderCardResult;
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
                        // include tags
                "AND CASE " +
                    "WHEN :includedSize = 0 THEN TRUE " +
                    "WHEN :includedSize > 0 THEN  " +
                        "o.asin IN ( " +
                            "SELECT b.asin " +
                            "FROM brg_product_tag_tag_group as b " +
                            "WHERE concat(b.tag_group_id, '-', b.tag_id) IN :tagIncluded " +
                            "GROUP BY b.asin HAVING COUNT(DISTINCT concat(b.tag_group_id, '-', b.tag_id)) = :includedSize " +
                        ") " +
                    "ELSE FALSE " +
                "END " +
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
                        // include tags
                "AND CASE " +
                    "WHEN :includedSize = 0 THEN TRUE " +
                    "WHEN :includedSize > 0 THEN  " +
                        "o.asin IN ( " +
                            "SELECT b.asin " +
                            "FROM brg_product_tag_tag_group as b " +
                            "WHERE concat(b.tag_group_id, '-', b.tag_id) IN :tagIncluded " +
                            "GROUP BY b.asin HAVING COUNT(DISTINCT concat(b.tag_group_id, '-', b.tag_id)) = :includedSize " +
                        ") " +
                    "ELSE FALSE " +
                "END " +
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
                        // include tags
                "AND CASE " +
                    "WHEN :includedSize = 0 THEN TRUE " +
                    "WHEN :includedSize > 0 THEN  " +
                        "o.asin IN ( " +
                            "SELECT b.asin " +
                            "FROM brg_product_tag_tag_group as b " +
                            "WHERE concat(b.tag_group_id, '-', b.tag_id) IN :tagIncluded " +
                            "GROUP BY b.asin HAVING COUNT(DISTINCT concat(b.tag_group_id, '-', b.tag_id)) = :includedSize " +
                        ") " +
                    "ELSE FALSE " +
                "END " +
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

@NamedNativeQuery(
        name = "get_all_product_analytics",
        query =
                "SELECT " +
                    "SUM(o.purchased - o.cancelled) AS quantitySold, " +
                    "o.title AS productName, " +
                    "SUM(o.royalties) AS royalties, " +
                    "p.price AS price, " +
                    "a.username AS amznAccUsername, " +
                    "o.asin AS asin " +
                "FROM orders AS o " +
                "LEFT JOIN products AS p " +
                "ON p.ASIN = o.asin " +
                "INNER JOIN amzn_users AS a " +
                "ON o.amzn_user_id = a.id " +
                "WHERE o.date >= :startDay AND o.date <= :endDay " +
                        //Include tags
                "AND CASE " +
                        "WHEN :includedSize = 0 THEN TRUE " +
                        "WHEN :includedSize > 0 THEN  " +
                            "o.asin IN ( " +
                                "SELECT b.asin " +
                                "FROM brg_product_tag_tag_group as b " +
                                "WHERE concat(b.tag_group_id, '-', b.tag_id) IN :tagIncluded " +
                                "GROUP BY b.asin HAVING COUNT(DISTINCT concat(b.tag_group_id, '-', b.tag_id)) = :includedSize " +
                            ") " +
                        "ELSE FALSE " +
                    "END " +
                        //Searchable
                "AND CASE " +
                        "WHEN :searchable THEN LOWER(o.asin) LIKE CONCAT('%',:key,'%') OR LOWER(o.title) LIKE CONCAT('%',:key,'%')" +
                        "ELSE TRUE " +
                    "END " +
                "GROUP BY o.asin, o.title, p.price, a.username, o.asin",
        resultSetMapping = "all_product_analytics"
)
@SqlResultSetMapping(
        name = "all_product_analytics",
        classes = @ConstructorResult(
                targetClass = ProductResult.class,
                columns = {
                        @ColumnResult(name = "quantitySold", type = Long.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "price", type = BigDecimal.class),
                        @ColumnResult(name = "amznAccUsername", type = String.class),
                        @ColumnResult(name = "asin", type = String.class),
                }
        )
)

@NamedNativeQuery(
        name = "get_group_product_analytics",
        query =
                "SELECT " +
                    "SUM(o.purchased - o.cancelled) AS quantitySold, " +
                    "o.title AS productName, " +
                    "SUM(o.royalties) AS royalties, " +
                    "p.price AS price, " +
                    "a.username AS amznAccUsername, " +
                    "o.asin AS asin " +
                "FROM orders AS o " +
                "LEFT JOIN products AS p " +
                "ON p.ASIN = o.asin " +
                "INNER JOIN amzn_users AS a " +
                "ON o.amzn_user_id = a.id " +
                "WHERE o.date >= :startDay AND o.date <= :endDay " +
                "AND o.amzn_user_id IN ( " +
                    "SELECT r.amzn_user_id " +
                    "FROM brg_group_amzn_user AS r " +
                    "WHERE r.group_id = :groupId " +
                ") " +
                        //Include tags
                "AND CASE " +
                        "WHEN :includedSize = 0 THEN TRUE " +
                        "WHEN :includedSize > 0 THEN  " +
                            "o.asin IN ( " +
                                "SELECT b.asin " +
                                "FROM brg_product_tag_tag_group as b " +
                                "WHERE concat(b.tag_group_id, '-', b.tag_id) IN :tagIncluded " +
                                "GROUP BY b.asin HAVING COUNT(DISTINCT concat(b.tag_group_id, '-', b.tag_id)) = :includedSize " +
                            ") " +
                        "ELSE FALSE " +
                    "END " +
                        //Searchable
                "AND CASE " +
                        "WHEN :searchable THEN LOWER(o.asin) LIKE CONCAT('%',:key,'%') OR LOWER(o.title) LIKE CONCAT('%',:key,'%')" +
                        "ELSE TRUE " +
                    "END " +
                "GROUP BY o.asin, o.title, p.price, a.username, o.asin",
        resultSetMapping = "group_product_analytics"
)
@SqlResultSetMapping(
        name = "group_product_analytics",
        classes = @ConstructorResult(
                targetClass = ProductResult.class,
                columns = {
                        @ColumnResult(name = "quantitySold", type = Long.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "price", type = BigDecimal.class),
                        @ColumnResult(name = "amznAccUsername", type = String.class),
                        @ColumnResult(name = "asin", type = String.class),
                }
        )
)

@NamedNativeQuery(
        name = "get_amzn_product_analytics",
        query =
                "SELECT " +
                    "SUM(o.purchased - o.cancelled) AS quantitySold, " +
                    "o.title AS productName, " +
                    "SUM(o.royalties) AS royalties, " +
                    "p.price AS price, " +
                    "a.username AS amznAccUsername, " +
                    "o.asin AS asin " +
                "FROM orders AS o " +
                "LEFT JOIN products AS p " +
                "ON p.ASIN = o.asin " +
                "INNER JOIN amzn_users AS a " +
                "ON o.amzn_user_id = a.id " +
                "WHERE o.date >= :startDay AND o.date <= :endDay " +
                "AND o.amzn_user_id = :amznId " +
                        //Include tags
                "AND CASE " +
                        "WHEN :includedSize = 0 THEN TRUE " +
                        "WHEN :includedSize > 0 THEN  " +
                            "o.asin IN ( " +
                                "SELECT b.asin " +
                                "FROM brg_product_tag_tag_group as b " +
                                "WHERE concat(b.tag_group_id, '-', b.tag_id) IN :tagIncluded " +
                                "GROUP BY b.asin HAVING COUNT(DISTINCT concat(b.tag_group_id, '-', b.tag_id)) = :includedSize " +
                            ") " +
                        "ELSE FALSE " +
                    "END " +
                        //Searchable
                "AND CASE " +
                        "WHEN :searchable THEN LOWER(o.asin) LIKE CONCAT('%',:key,'%') OR LOWER(o.title) LIKE CONCAT('%',:key,'%')" +
                        "ELSE TRUE " +
                    "END " +
                "GROUP BY o.asin, o.title, p.price, a.username, o.asin",
        resultSetMapping = "amzn_product_analytics"
)
@SqlResultSetMapping(
        name = "amzn_product_analytics",
        classes = @ConstructorResult(
                targetClass = ProductResult.class,
                columns = {
                        @ColumnResult(name = "quantitySold", type = Long.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "price", type = BigDecimal.class),
                        @ColumnResult(name = "amznAccUsername", type = String.class),
                        @ColumnResult(name = "asin", type = String.class),
                }
        )
)

@NamedNativeQuery(
        name = "get_all_order_cart_result",
        query =
            "SELECT " +
                "SUM(o.purchased - o.cancelled) AS sold, " +
                "SUM(o.purchased) AS purchased, " +
                "SUM(o.cancelled) AS cancelled, " +
                "SUM(o.returned) AS returned, " +
                "SUM(o.royalties) AS royalties " +
            "FROM orders AS o " +
            "WHERE o.date >= :startDate AND o.date <= :endDate " +
                    //include tags
            "AND CASE " +
                "WHEN :includedSize = 0 THEN TRUE " +
                "WHEN :includedSize > 0 THEN  " +
                    "o.asin IN ( " +
                        "SELECT b.asin " +
                        "FROM brg_product_tag_tag_group as b " +
                        "WHERE concat(b.tag_group_id, '-', b.tag_id) IN :tagIncluded " +
                        "GROUP BY b.asin HAVING COUNT(DISTINCT concat(b.tag_group_id, '-', b.tag_id)) = :includedSize " +
                    ") " +
                "ELSE FALSE " +
            "END ",
        resultSetMapping = "all_order_cart_result"
)
@SqlResultSetMapping(
        name = "all_order_cart_result",
        classes = @ConstructorResult(
                targetClass = OrderCardResult.class,
                columns = {
                        @ColumnResult(name = "sold", type = Long.class),
                        @ColumnResult(name = "purchased", type = Long.class),
                        @ColumnResult(name = "cancelled", type = Long.class),
                        @ColumnResult(name = "returned", type = Long.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                }
        )
)

@NamedNativeQuery(
        name = "get_group_order_cart_result",
        query =
            "SELECT " +
                "SUM(o.purchased - o.cancelled) AS sold, " +
                "SUM(o.purchased) AS purchased, " +
                "SUM(o.cancelled) AS cancelled, " +
                "SUM(o.returned) AS returned, " +
                "SUM(o.royalties) AS royalties " +
            "FROM orders AS o " +
            "WHERE o.date >= :startDate AND o.date <= :endDate " +
            "AND o.amzn_user_id IN ( " +
                "SELECT r.amzn_user_id " +
                "FROM brg_group_amzn_user AS r " +
                "WHERE r.group_id = :groupId " +
            ") " +
                    //include tags
            "AND CASE " +
                "WHEN :includedSize = 0 THEN TRUE " +
                "WHEN :includedSize > 0 THEN  " +
                    "o.asin IN ( " +
                        "SELECT b.asin " +
                        "FROM brg_product_tag_tag_group as b " +
                        "WHERE concat(b.tag_group_id, '-', b.tag_id) IN :tagIncluded " +
                        "GROUP BY b.asin HAVING COUNT(DISTINCT concat(b.tag_group_id, '-', b.tag_id)) = :includedSize " +
                    ") " +
                "ELSE FALSE " +
            "END ",
        resultSetMapping = "group_order_cart_result"
)
@SqlResultSetMapping(
        name = "group_order_cart_result",
        classes = @ConstructorResult(
                targetClass = OrderCardResult.class,
                columns = {
                        @ColumnResult(name = "sold", type = Long.class),
                        @ColumnResult(name = "purchased", type = Long.class),
                        @ColumnResult(name = "cancelled", type = Long.class),
                        @ColumnResult(name = "returned", type = Long.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                }
        )
)

@NamedNativeQuery(
        name = "get_amzn_order_cart_result",
        query =
            "SELECT " +
                "SUM(o.purchased - o.cancelled) AS sold, " +
                "SUM(o.purchased) AS purchased, " +
                "SUM(o.cancelled) AS cancelled, " +
                "SUM(o.returned) AS returned, " +
                "SUM(o.royalties) AS royalties " +
            "FROM orders AS o " +
            "WHERE o.date >= :startDate AND o.date <= :endDate " +
            "AND o.amzn_user_id = :amznId " +
                    //include tags
            "AND CASE " +
                "WHEN :includedSize = 0 THEN TRUE " +
                "WHEN :includedSize > 0 THEN  " +
                    "o.asin IN ( " +
                        "SELECT b.asin " +
                        "FROM brg_product_tag_tag_group as b " +
                        "WHERE concat(b.tag_group_id, '-', b.tag_id) IN :tagIncluded " +
                        "GROUP BY b.asin HAVING COUNT(DISTINCT concat(b.tag_group_id, '-', b.tag_id)) = :includedSize " +
                    ") " +
                "ELSE FALSE " +
            "END ",
        resultSetMapping = "amzn_order_cart_result"
)
@SqlResultSetMapping(
        name = "amzn_order_cart_result",
        classes = @ConstructorResult(
                targetClass = OrderCardResult.class,
                columns = {
                        @ColumnResult(name = "sold", type = Long.class),
                        @ColumnResult(name = "purchased", type = Long.class),
                        @ColumnResult(name = "cancelled", type = Long.class),
                        @ColumnResult(name = "returned", type = Long.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                }
        )
)

public class OrderNativeQueryDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
}
