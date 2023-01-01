package com.unimerch.repository.native_query_dto.product;

import com.unimerch.dto.analytics.ProductAnalyticsResult;
import com.unimerch.dto.product.ProductResult;
import com.unimerch.repository.model.BrgProductTag;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "product_native_query_dto")

@NamedNativeQuery(
        name = "get_product_item_result",
        query =
                "SELECT  " +
                        "SUM(o.purchased - o.cancelled) AS quantitySold, " +
                        "o.title AS productName, " +
                        "SUM(o.royalties) AS royalties, " +
                        "p.price AS price, " +
                        "o.amzn_user_id AS amznAccUsername, " +
                        "o.ASIN AS asin " +
                        "FROM orders AS o " +
                        "LEFT JOIN products AS p " +
                        "ON p.ASIN = o.ASIN " +
                        "WHERE o.amzn_user_id IN (:amznAccIds) " +
                        "AND o.date >= :startDay " +
                        "GROUP BY o.ASIN, o.title, p.price, o.amzn_user_id",
        resultSetMapping = "product_item_result"
)
@SqlResultSetMapping(
        name = "product_item_result",
        classes = @ConstructorResult(
                targetClass = ProductResult.class,
                columns = {
                        @ColumnResult(name = "quantitySold", type = Integer.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "royalties", type = BigDecimal.class),
                        @ColumnResult(name = "price", type = BigDecimal.class),
                        @ColumnResult(name = "amznAccUsername", type = String.class),
                        @ColumnResult(name = "asin", type = String.class)
                }
        )
)

public class ProductNativeQueryDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
}
